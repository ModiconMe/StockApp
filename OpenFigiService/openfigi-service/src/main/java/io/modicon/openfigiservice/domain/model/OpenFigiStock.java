package io.modicon.openfigiservice.domain.model;

import lombok.Builder;

@Builder
public record OpenFigiStock(
        String figi,
        String securityType,
        String marketSector,
        String ticker,
        String name,
        String exchCode,
        String shareClassFIGI,
        String compositeFIGI,
        String securityType2,
        String securityDescription
) {
}
