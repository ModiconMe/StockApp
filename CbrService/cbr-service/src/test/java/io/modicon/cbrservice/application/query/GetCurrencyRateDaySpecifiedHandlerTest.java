package io.modicon.cbrservice.application.query;

import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecified;
import io.modicon.cbrservice.application.service.CbrCurrencyService;
import io.modicon.cbrservice.model.CurrencyRate;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCurrencyRateDaySpecifiedHandlerTest {

    @Mock
    private CbrCurrencyService cbrCurrencyService;
    private GetCurrencyRateDaySpecifiedHandler getCurrencyRateDaySpecifiedHandler;

    @BeforeEach
    void setUp() {
        getCurrencyRateDaySpecifiedHandler = new GetCurrencyRateDaySpecifiedHandler(cbrCurrencyService);
    }

    Set<CurrencyRate> currencyRates = Set.of(
            new CurrencyRate("Австралийский доллар", "AUD", BigDecimal.valueOf(49.0935)),
            new CurrencyRate("Азербайджанский манат", "AZN", BigDecimal.valueOf(40.6625)),
            new CurrencyRate("Фунт стерлингов Соединенного королевства", "GBP", BigDecimal.valueOf(85.4263))
    );

    Set<CurrencyRateDto> currencyRatesDto = Set.of(
            new CurrencyRateDto("Австралийский доллар", "AUD", BigDecimal.valueOf(49.0935)),
            new CurrencyRateDto("Азербайджанский манат", "AZN", BigDecimal.valueOf(40.6625)),
            new CurrencyRateDto("Фунт стерлингов Соединенного королевства", "GBP", BigDecimal.valueOf(85.4263))
    );


    @Test
    void should_getCurrencyDaySpecified_whenProvidedDateValid() {
        String date = "20/10/2002";
        when(cbrCurrencyService.getCurrencyRatesDaySpecified(date)).thenReturn(currencyRates);
        Set<CurrencyRateDto> result = getCurrencyRateDaySpecifiedHandler.handle(new GetCurrencyRateDaySpecified(date)).getCurrencyRates();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(currencyRatesDto.size());
        assertThat(result).isEqualTo(currencyRatesDto);
    }
}