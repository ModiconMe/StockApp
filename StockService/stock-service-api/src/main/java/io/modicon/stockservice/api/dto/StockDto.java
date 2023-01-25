package io.modicon.stockservice.api.dto;

import lombok.Builder;

@Builder
public record StockDto(
    String ticker,
    String figi,
    String name,
    String type,
    CurrencyDto currency,
    String source
) { }
