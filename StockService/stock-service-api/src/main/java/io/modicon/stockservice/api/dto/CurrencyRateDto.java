package io.modicon.stockservice.api.dto;

import java.math.BigDecimal;

public record CurrencyRateDto(String name, String ticker, BigDecimal rate) {
}
