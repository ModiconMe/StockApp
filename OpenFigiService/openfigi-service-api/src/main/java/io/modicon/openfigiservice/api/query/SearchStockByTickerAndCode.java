package io.modicon.openfigiservice.api.query;

import io.modicon.cqrsbus.Query;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchStockByTickerAndCode implements Query<SearchStockByTickerAndCodeResult> {

    private SearchStockDto stock;

}
