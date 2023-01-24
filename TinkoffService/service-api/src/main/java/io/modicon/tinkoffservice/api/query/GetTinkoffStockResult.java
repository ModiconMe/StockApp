package io.modicon.tinkoffservice.api.query;

import io.modicon.stockservice.api.dto.StockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTinkoffStockResult {

    private StockDto stock;

}
