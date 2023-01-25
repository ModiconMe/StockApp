package io.modicon.moexservice.domain.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Bond(String ticker, BigDecimal price, String name) {
}
