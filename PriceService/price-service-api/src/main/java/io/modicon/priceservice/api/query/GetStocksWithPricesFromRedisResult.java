package io.modicon.priceservice.api.query;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStocksWithPricesFromRedisResult {

    private Set<StockWithPriceDto> stockPrices;

}
