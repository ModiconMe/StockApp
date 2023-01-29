package io.modicon.telegrambot.application.client;

import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.userservice.operation.CurrencyOperation;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "currency-operations",
        url = "${api.userService.currencyOperations}",
        configuration = ApplicationConfig.class)
public interface CurrencyOperationsClient extends CurrencyOperation {
}
