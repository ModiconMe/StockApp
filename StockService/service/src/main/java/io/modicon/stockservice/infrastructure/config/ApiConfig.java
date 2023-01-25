package io.modicon.stockservice.infrastructure.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "api")
public class ApiConfig {
//    private TinkoffConfig tinkoffConfig;
//    private MoexConfig moexConfig;
}
