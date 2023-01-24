package io.modicon.stockservice.application.client;

import io.modicon.moexservice.api.operation.MoexServiceOperation;
import io.modicon.stockservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "moex-service",
        url = "${api.moexConfig.moexService}",
        configuration = ApplicationConfig.class)
public interface MoexServiceClient extends MoexServiceOperation {
}
