package io.modicon.tinkoffservice.infrastructure.config;

import io.modicon.cqrsbus.Bus;
import io.modicon.cqrsbus.DefaultBus;
import io.modicon.cqrsbus.Registry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.tinkoff.piapi.core.InvestApi;

@RequiredArgsConstructor
@Configuration
@EnableAsync
@EnableConfigurationProperties(TinkoffApiConfig.class)
public class ApplicationConfig {

    private final TinkoffApiClientFactory apiFactory;
    private final ApplicationContext applicationContext;

    @Bean
    public InvestApi api() {
        var ssoToken = System.getenv("ssoToken");
        return apiFactory.getApi(ssoToken);
    }

    @Bean
    public Bus bus() {
        return new DefaultBus(new Registry(applicationContext));
    }
}