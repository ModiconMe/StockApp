package io.modicon.userservice.application.service;

import io.modicon.userservice.domain.model.Currency;
import io.modicon.userservice.domain.model.StockWithPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PortfolioCostService {

    private final CurrencyService currencyService;

    /**
     * @param stocks stock with prices and currencies
     * @return map [figi, costInRub]
     */
    public Map<String, BigDecimal> getPositionsCostInRub(List<StockWithPrice> stocks) {
        Map<String, BigDecimal> figiPrice = stocks.stream().collect(Collectors.toMap(StockWithPrice::figi, StockWithPrice::price));
        Map<String, Currency> figiCurrency = stocks.stream().collect(Collectors.toMap(StockWithPrice::figi, StockWithPrice::currency));

        // get cost of currency
        Map<Currency, BigDecimal> currencyCost = currencyService.getCurrencyCost();
        Map<String, BigDecimal> figiCurrencyCost = figiCurrency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> currencyCost.get(Currency.valueOf(entry.getValue().getCurrency().toUpperCase()))));

        return figiPrice.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> figiCurrencyCost.get(entry.getKey()).multiply(entry.getValue())));
    }

}
