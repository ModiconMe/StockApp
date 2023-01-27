package io.modicon.userservice.application.client;

import io.modicon.cbrservice.api.operation.CbrCurrencyOperation;
import io.modicon.userservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "currency-service",
        url = "${api.currencyConfig.currencyService}",
        configuration = ApplicationConfig.class)
public interface CurrencyServiceClient extends CbrCurrencyOperation {
}
