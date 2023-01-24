package io.modicon.moexservice.api.dto;

import lombok.Builder;

@Builder
public record BondDto(
    String ticker,
    String figi,
    String name,
    String type,
    CurrencyDto currency,
    String source
) { }
