package io.modicon.stockservice.infrastructure.config;

import lombok.Getter;

@Getter
public class TinkoffConfig {
    private String tinkoffService;
    private String getStocksByTickers;
}
