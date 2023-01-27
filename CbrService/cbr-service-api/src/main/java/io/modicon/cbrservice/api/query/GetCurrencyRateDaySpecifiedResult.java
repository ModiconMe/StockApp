package io.modicon.cbrservice.api.query;

import io.modicon.stockservice.api.dto.CurrencyRateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetCurrencyRateDaySpecifiedResult {

    private Set<CurrencyRateDto> currencyRates;

}
