package io.modicon.stockservice.model;

import lombok.Builder;

@Builder
public record Stock(
    String ticker,
    String figi,
    String name,
    String type,
    Currency currency,
    String source
) { }
