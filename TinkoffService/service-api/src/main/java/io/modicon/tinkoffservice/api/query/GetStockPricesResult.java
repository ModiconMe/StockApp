package io.modicon.tinkoffservice.api.query;

import io.modicon.tinkoffservice.api.dto.StockPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStockPricesResult {

    private List<StockPriceDto> stockPrices;

}
