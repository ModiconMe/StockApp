package io.modicon.stockservice.api.query;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStocksWithPricesResult {

    private List<StockWithPriceDto> stocks;
    private List<String> notFoundFigis;

}
