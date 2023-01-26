package io.modicon.priceservice.domain.model;

import io.modicon.stockservice.api.dto.CurrencyDto;
import lombok.Builder;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RedisHash("Stock")
@Builder
public record StockWithPrice(
        @Id
        String figi,
        String ticker,
        String name,
        String type,
        CurrencyDto currency,
        String source,
        BigDecimal price,
        LocalDateTime updatedAt
) {
}
