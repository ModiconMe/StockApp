package io.modicon.tinkoffservice.api.query;

import io.modicon.tinkoffservice.api.dto.StockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStockResult {

    private StockDto stock;

}
