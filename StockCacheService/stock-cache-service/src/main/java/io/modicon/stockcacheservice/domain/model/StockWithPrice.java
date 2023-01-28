package io.modicon.stockcacheservice.domain.model;

import io.modicon.stockservice.api.dto.CurrencyDto;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

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
        BigDecimal price
) {
}
