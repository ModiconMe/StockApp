package io.modicon.telegrambot.application.client;

import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.userservice.operation.UserOperation;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-operations",
        url = "${api.userService.userOperations}",
        configuration = ApplicationConfig.class)
public interface UserOperationsClient extends UserOperation {
}
