package io.modicon.stockservice.api.dto;

import java.math.BigDecimal;

public record StockPriceDto(String figi, BigDecimal price) {
}
