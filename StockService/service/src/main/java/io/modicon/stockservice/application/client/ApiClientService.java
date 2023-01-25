package io.modicon.stockservice.application.client;

import org.springframework.stereotype.Component;

@Component
public record ApiClientService(MoexServiceClient moexService, TinkoffServiceClient tinkoffService) {
}
