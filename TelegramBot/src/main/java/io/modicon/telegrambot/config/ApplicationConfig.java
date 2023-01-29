package io.modicon.telegrambot.config;

import feign.codec.ErrorDecoder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application.yml")
@EnableFeignClients(basePackages = "io.modicon.telegrambot.application.client")
@Getter
public class ApplicationConfig {
    @Value("${telegram.bot-name}")
    private String botName;
    @Value("${telegram.api-key}")
    private String apiKey;
    @Value("${telegram.bot-owner}")
    private Long botOwner;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new TelegramBotExceptionDecoder();
    }
}
