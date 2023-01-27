package io.modicon.cbrservice.application.client;

import io.modicon.cbrservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-rates",
        url = "${cbr.currency.url}",
        configuration = ApplicationConfig.class)
public interface CbrCurrencyRatesClient {
    @GetMapping
    String getCurrentCurrencyRate();
    @GetMapping
    String getCurrencyRateDaySpecified(@RequestParam(name = "date_req") String date);
}
