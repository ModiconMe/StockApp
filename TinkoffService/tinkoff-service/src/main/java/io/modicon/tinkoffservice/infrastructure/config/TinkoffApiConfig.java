package io.modicon.tinkoffservice.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.core.InvestApi;

@Getter
@Component
@ConfigurationProperties(prefix = "api")
public class TinkoffApiConfig {

    @Value("${api.type}")
    private String apiType;

}

@Slf4j
@RequiredArgsConstructor
@Component
class TinkoffApiClientFactory {

    private final TinkoffApiConfig tinkoffApiConfig;

    public InvestApi getApi(String token) {
        String apiType = tinkoffApiConfig.getApiType();
        switch (apiType) {
            case "api" -> {
                log.info("create tinkoff api");
                return InvestApi.create(token);
            }
            case "sandbox" -> {
                log.info("create tinkoff sandbox");
                return InvestApi.createSandbox(token);
            }
            case "readonlyApi" -> {
                log.info("create tinkoff readOnlyApi");
                return InvestApi.createReadonly(token);
            }
        }
        throw new IllegalArgumentException("Api type is not valid: apiType:" + apiType);
    }
}
