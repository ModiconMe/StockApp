package io.modicon.openfigiservice.api.dto;

import lombok.Builder;

@Builder
public record FoundedStockDto(
        String figi,
        String ticker,
        String exchCode,
        String name
) {
}
