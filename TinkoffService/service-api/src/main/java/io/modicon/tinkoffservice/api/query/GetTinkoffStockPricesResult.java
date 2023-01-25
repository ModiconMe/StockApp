package io.modicon.tinkoffservice.api.query;

import io.modicon.stockservice.api.dto.StockPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTinkoffStockPricesResult {

    private List<StockPriceDto> stockPrices;

}
