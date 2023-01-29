package io.modicon.telegrambot.application.client;

import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.userservice.operation.StockSearchOperation;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "stocks-operations",
        url = "${api.userService.stockSearchOperations}",
        configuration = ApplicationConfig.class)
public interface StockSearchOperationsClient extends StockSearchOperation {
}
