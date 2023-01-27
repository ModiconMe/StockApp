package io.modicon.openfigiservice.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record SearchStockDto(
        @NotEmpty(message = "ticker should be not empty")
        String ticker,
        @NotEmpty(message = "exchCode should be not empty")
        String exchCode
) {
}
