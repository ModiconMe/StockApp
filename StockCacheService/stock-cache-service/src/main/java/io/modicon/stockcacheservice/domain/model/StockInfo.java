package io.modicon.stockcacheservice.domain.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("StockInfo")
@Builder
public record StockInfo(
        @Id
        String figi,
        String ticker,
        String name,
        String exchCode
) {
}
