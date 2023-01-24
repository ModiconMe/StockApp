package io.modicon.stockservice.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StockWithPriceDto(
        String ticker,
        String figi,
        String name,
        String type,
        CurrencyDto currency,
        String source,
        BigDecimal price
) {
}
