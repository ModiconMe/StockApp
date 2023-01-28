package io.modicon.userservice.application.service;

import io.modicon.stockservice.api.dto.CurrencyRateDto;
import io.modicon.userservice.application.client.CurrencyServiceClient;
import io.modicon.userservice.domain.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CbrCurrencyService implements CurrencyService {

    private final CurrencyServiceClient currencyServiceClient;

    @Override
    public Map<Currency, BigDecimal> getCurrencyCost() {
        Map<String, BigDecimal> allCurrencyRates = currencyServiceClient.getCurrentCurrencyRate().getCurrencyRates()
                .stream().collect(Collectors.toMap(CurrencyRateDto::ticker, CurrencyRateDto::rate));

        Map<Currency, BigDecimal> appCurrencyRates = new HashMap<>();
        Currency[] values = Currency.values();
        for (Currency c : values) {
            appCurrencyRates.put(c, allCurrencyRates.get(c.getCurrency()));
        }
        appCurrencyRates.put(Currency.RUB, BigDecimal.valueOf(1));
        return appCurrencyRates;
    }

    public Set<CurrencyRateDto> getCurrentCurrencyRates() {
        return currencyServiceClient.getCurrentCurrencyRate().getCurrencyRates();
    }

    public Set<CurrencyRateDto> getCurrencyRatesDaySpecified(String date) {
        return currencyServiceClient.getCurrencyRateDaySpecified(date).getCurrencyRates();
    }
}
