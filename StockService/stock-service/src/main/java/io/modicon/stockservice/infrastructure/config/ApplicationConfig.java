package io.modicon.stockservice.infrastructure.config;

import feign.Logger;
import io.modicon.cqrsbus.Bus;
import io.modicon.cqrsbus.DefaultBus;
import io.modicon.cqrsbus.Registry;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableFeignClients(basePackages = "io.modicon.stockservice.application.client")
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