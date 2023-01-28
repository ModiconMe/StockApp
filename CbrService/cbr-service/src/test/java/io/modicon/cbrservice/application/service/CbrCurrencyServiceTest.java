package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.application.client.CbrCurrencyRatesClient;
import io.modicon.cbrservice.infrastructure.exception.ApiException;
import io.modicon.cbrservice.model.CurrencyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CbrCurrencyServiceTest {

    @Mock
    private CbrCurrencyRatesClient cbrCurrencyRatesClient;
    @Mock
    private CbrCurrencyParserImpl cbrCurrencyParser;

    private CbrCurrencyService cbrCurrencyService;

    @BeforeEach
    void setUp() {
        cbrCurrencyService = new CbrCurrencyService(cbrCurrencyRatesClient, cbrCurrencyParser);
    }

    private String xmlFromCbr = """
            <ValCurs Date="27.01.2023" name="Foreign Currency Market">
                <Valute ID="R01010">
                <NumCode>036</NumCode>
                <CharCode>AUD</CharCode>
                <Nominal>1</Nominal>
                <Name>Австралийский доллар</Name>
                <Value>49,0935</Value>
            </Valute>
                <Valute ID="R01020A">
                <NumCode>944</NumCode>
                <CharCode>AZN</CharCode>
                <Nominal>1</Nominal>
                <Name>Азербайджанский манат</Name>
                <Value>40,6625</Value>
            </Valute>
                <Valute ID="R01035">
                <NumCode>826</NumCode>
                <CharCode>GBP</CharCode>
                <Nominal>1</Nominal>
                <Name>Фунт стерлингов Соединенного королевства</Name>
                <Value>85,4263</Value>
            </Valute>
            </ValCurs>
            """;

    Set<CurrencyRate> parseResult = Set.of(
            new CurrencyRate("Австралийский доллар", "AUD", BigDecimal.valueOf(49.0935)),
            new CurrencyRate("Азербайджанский манат", "AZN", BigDecimal.valueOf(40.6625)),
            new CurrencyRate("Фунт стерлингов Соединенного королевства", "GBP", BigDecimal.valueOf(85.4263))
    );

    @Test
    void should_getCurrentCurrencyRates() {
        when(cbrCurrencyRatesClient.getCurrentCurrencyRate()).thenReturn(xmlFromCbr);
        when(cbrCurrencyParser.parse(xmlFromCbr)).thenReturn(parseResult);

        Set<CurrencyRate> result = cbrCurrencyService.getCurrentCurrencyRates();

        verify(cbrCurrencyParser, times(1)).parse(xmlFromCbr);
        verify(cbrCurrencyRatesClient, times(1)).getCurrentCurrencyRate();
        assertThat(result).isEqualTo(parseResult);
    }

    @Test
    void should_getCurrencyRatesDaySpecified() {
        String date = "25/04/2002";
        when(cbrCurrencyRatesClient.getCurrencyRateDaySpecified(date)).thenReturn(xmlFromCbr);
        when(cbrCurrencyParser.parse(xmlFromCbr)).thenReturn(parseResult);

        Set<CurrencyRate> result = cbrCurrencyService.getCurrencyRatesDaySpecified(date);

        verify(cbrCurrencyParser, times(1)).parse(xmlFromCbr);
        verify(cbrCurrencyRatesClient, times(1)).getCurrencyRateDaySpecified(date);
        assertThat(result).isEqualTo(parseResult);
    }

    @Test
    void should_throw_whenSetOfCurrenciesIsEmpty_Current() {
        when(cbrCurrencyRatesClient.getCurrentCurrencyRate()).thenReturn(xmlFromCbr);
        when(cbrCurrencyParser.parse(xmlFromCbr)).thenReturn(new HashSet<>());

        ApiException exception = catchThrowableOfType(() -> cbrCurrencyService.getCurrentCurrencyRates(), ApiException.class);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void should_throw_whenSetOfCurrenciesIsEmpty() {
        String date = "25/04/2002";
        when(cbrCurrencyRatesClient.getCurrencyRateDaySpecified(date)).thenReturn(xmlFromCbr);
        when(cbrCurrencyParser.parse(xmlFromCbr)).thenReturn(new HashSet<>());

        ApiException exception = catchThrowableOfType(() -> cbrCurrencyService.getCurrencyRatesDaySpecified(date), ApiException.class);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }
}