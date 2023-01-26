package io.modicon.userservice.domain.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StockWithPrice(
        String ticker,
        String figi,
        String name,
        TypeEntity type,
        Currency currency,
        String source,
        BigDecimal price
) {
}
