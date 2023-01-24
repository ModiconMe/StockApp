package io.modicon.stockservice.infrastructure.config;

import lombok.Getter;

@Getter
public class MoexConfig {
    private String moexService;
    private String getStocksByTickers;
}
