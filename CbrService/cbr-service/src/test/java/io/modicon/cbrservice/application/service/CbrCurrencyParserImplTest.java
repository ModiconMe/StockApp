package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.infrastructure.exception.ApiException;
import io.modicon.cbrservice.model.CurrencyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class CbrCurrencyParserImplTest {

    private CbrCurrencyParserImpl cbrCurrencyParser;

    @BeforeEach
    void setUp() {
        cbrCurrencyParser = new CbrCurrencyParserImpl();
    }

    private String validXml = """
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

    private String invalidXml = """
            <ValCurs Date="27.01.2023" name="Foreign Currency Market">
                <Valute ID="R01010">
                <NumCode>036</NumCode>
                <CharCode>AUD</CharCode>
                <Nominal>1</Nominal>
                <Name>Австралийский доллар</Name>
                <Value>49,0935</Value>
            
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


    @Test
    void should_parseCurrency_whenXmlIsValid() {
        Set<CurrencyRate> parsedRates = cbrCurrencyParser.parse(validXml);
        assertThat(parsedRates).isNotEmpty();

        Set<CurrencyRate> expected = Set.of(
                new CurrencyRate("Австралийский доллар", "AUD", BigDecimal.valueOf(49.0935)),
                new CurrencyRate("Азербайджанский манат", "AZN", BigDecimal.valueOf(40.6625)),
                new CurrencyRate("Фунт стерлингов Соединенного королевства", "GBP", BigDecimal.valueOf(85.4263))
        );
        assertThat(parsedRates.size()).isEqualTo(expected.size());
        assertThat(parsedRates).isEqualTo(expected);
    }

    @Test
    void should_throw_whenXmlIsInvalid() {
        ApiException exception = catchThrowableOfType(() -> cbrCurrencyParser.parse(invalidXml), ApiException.class);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}