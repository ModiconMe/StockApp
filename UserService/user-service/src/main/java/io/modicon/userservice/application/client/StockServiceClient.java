package io.modicon.userservice.application.client;

import io.modicon.stockservice.api.operation.StockBondOperation;
import io.modicon.userservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "stock-service",
        url = "${api.stockConfig.stockService}",
        configuration = ApplicationConfig.class)
public interface StockServiceClient extends StockBondOperation {
}
