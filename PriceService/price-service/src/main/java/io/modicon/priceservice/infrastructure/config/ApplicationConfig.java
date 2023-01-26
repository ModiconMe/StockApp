package io.modicon.priceservice.infrastructure.config;

import io.modicon.cqrsbus.Bus;
import io.modicon.cqrsbus.DefaultBus;
import io.modicon.cqrsbus.Registry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableCaching
public class ApplicationConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public Bus bus() {
        return new DefaultBus(new Registry(applicationContext));
    }
}