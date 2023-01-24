package io.modicon.moexservice.infrastructure.config;

import feign.Logger;
import io.modicon.cqrsbus.Bus;
import io.modicon.cqrsbus.DefaultBus;
import io.modicon.cqrsbus.Registry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@RequiredArgsConstructor
@Configuration
@EnableCaching
@EnableFeignClients(basePackages = "io.modicon.moexservice.application.client")
public class ApplicationConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Bus bus() {
        return new DefaultBus(new Registry(applicationContext));
    }
}