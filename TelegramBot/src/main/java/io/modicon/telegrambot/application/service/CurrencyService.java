package io.modicon.telegrambot.application.service;

import io.modicon.stockservice.api.dto.CurrencyRateDto;
import io.modicon.telegrambot.application.client.CurrencyOperationsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyOperationsClient currencyOperationsClient;

    public Set<CurrencyRateDto> getCurrentCurrencyRates() {
        return currencyOperationsClient.getCurrentCurrencyRate().getCurrencyRates();
    }

    public Set<CurrencyRateDto> getCurrentCurrencyRatesDateSpecified(String date) {
        return currencyOperationsClient.getCurrencyRateDaySpecified(date).getCurrencyRates();
    }
}
