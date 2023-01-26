package io.modicon.priceservice.api.query;

import io.modicon.cqrsbus.Command;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PutStocksWithPricesToRedis implements Command<PutStocksWithPricesToRedisResult> {

    private List<StockWithPriceDto> stocks;

}
