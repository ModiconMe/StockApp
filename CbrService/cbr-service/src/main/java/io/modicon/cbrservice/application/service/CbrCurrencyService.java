package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.application.client.CbrCurrencyRatesClient;
import io.modicon.cbrservice.infrastructure.exception.ApiException;
import io.modicon.cbrservice.model.CurrencyRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CbrCurrencyService {

    private final CbrCurrencyRatesClient cbrCurrencyRatesClient;
    private final CurrencyParser parser;

    @Cacheable(value = "current_rates")
    public Set<CurrencyRate> getCurrentCurrencyRates() {
        log.info("Getting corporate bonds from MOEX");
        String xmlFromCbr = cbrCurrencyRatesClient.getCurrentCurrencyRate();
        Set<CurrencyRate> currencyRates = parser.parse(xmlFromCbr);
        if (currencyRates.isEmpty()) {
            log.error("Cbr isn't answering for getting cbr currencies bonds");
            throw ApiException.exception(HttpStatus.TOO_MANY_REQUESTS, "Cbr isn't answering for getting cbr currencies bonds");
        }
        return currencyRates;
    }

    public Set<CurrencyRate> getCurrencyRatesDaySpecified(String date) {
        log.info("Getting corporate bonds from MOEX");
        String xmlFromCbr = cbrCurrencyRatesClient.getCurrencyRateDaySpecified(date);
        Set<CurrencyRate> currencyRates = parser.parse(xmlFromCbr);
        if (currencyRates.isEmpty()) {
            log.error("Cbr isn't answering for getting cbr currencies bonds");
            throw ApiException.exception(HttpStatus.TOO_MANY_REQUESTS, "Cbr isn't answering for getting cbr currencies bonds");
        }
        return currencyRates;
    }
}
