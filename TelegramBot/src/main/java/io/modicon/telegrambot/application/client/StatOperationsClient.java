package io.modicon.telegrambot.application.client;

import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.userservice.operation.PortfolioStatisticOperation;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "stat-operations",
        url = "${api.userService.portfolioStatOperations}",
        configuration = ApplicationConfig.class)
public interface StatOperationsClient extends PortfolioStatisticOperation {
}
