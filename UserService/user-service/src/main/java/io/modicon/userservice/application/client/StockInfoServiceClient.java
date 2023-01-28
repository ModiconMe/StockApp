package io.modicon.userservice.application.client;

import io.modicon.priceservice.api.operation.SearchServiceOperation;
import io.modicon.userservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "price-service",
        url = "${api.stockInfoConfig.stockInfoService}",
        configuration = ApplicationConfig.class)
public interface StockInfoServiceClient extends SearchServiceOperation {
}
