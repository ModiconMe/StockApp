package io.modicon.priceservice.application.client;

import io.modicon.priceservice.infrastructure.config.ApplicationConfig;
import io.modicon.tinkoffservice.api.operation.TinkoffServiceOperation;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "tinkoff-service",
        url = "${api.tinkoffConfig.tinkoffService}",
        configuration = ApplicationConfig.class)
public interface TinkoffServiceClient extends TinkoffServiceOperation {
}
