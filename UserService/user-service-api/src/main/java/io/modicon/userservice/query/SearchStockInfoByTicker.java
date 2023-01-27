package io.modicon.userservice.query;

import io.modicon.cqrsbus.Query;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchStockInfoByTicker implements Query<SearchStockInfoByTickerResult> {
    private SearchStockDto stock;
}
