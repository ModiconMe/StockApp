package io.modicon.stockservice.application.client;

import io.modicon.priceservice.api.operation.PriceServiceOperation;
import io.modicon.stockservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "price-service",
        url = "${api.priceConfig.priceService}",
        configuration = ApplicationConfig.class)
public interface PriceServiceClient extends PriceServiceOperation {
}
