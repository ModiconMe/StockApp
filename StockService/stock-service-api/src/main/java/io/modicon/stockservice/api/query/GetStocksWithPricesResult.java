package io.modicon.stockservice.api.query;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStocksWithPricesResult {

    private List<StockWithPriceDto> stocks;
    private Set<String> notFoundFigis;

}
