package io.modicon.stockservice.api.query;

import io.modicon.stockservice.api.dto.StockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStocksResult {

    private List<StockDto> stocks;
    private List<String> notFoundFigis;

}
