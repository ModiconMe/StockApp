package io.modicon.userservice.application.client;

import io.modicon.openfigiservice.api.operation.OpenFigiOperation;
import io.modicon.userservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "open-figi-service",
        url = "${api.openFigiConfig.openFigiService}",
        configuration = ApplicationConfig.class)
public interface OpenFigiServiceClient extends OpenFigiOperation {
}
