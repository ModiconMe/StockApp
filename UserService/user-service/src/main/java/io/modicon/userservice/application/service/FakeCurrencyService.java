package io.modicon.userservice.application.service;

import io.modicon.userservice.domain.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FakeCurrencyService implements CurrencyService {

    @Override
    public Map<Currency, BigDecimal> getCurrencyCost() {
        return Map.of(
                Currency.RUB, BigDecimal.valueOf(1),
                Currency.USD, BigDecimal.valueOf(65),
                Currency.EUR, BigDecimal.valueOf(75),
                Currency.GBP, BigDecimal.valueOf(86),
                Currency.CNY, BigDecimal.valueOf(10),
                Currency.CHF, BigDecimal.valueOf(76),
                Currency.HKD, BigDecimal.valueOf(99),
                Currency.JPY, BigDecimal.valueOf(53)
        );
    }

}
