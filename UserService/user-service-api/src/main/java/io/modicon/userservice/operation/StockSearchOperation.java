package io.modicon.userservice.operation;

import io.modicon.userservice.query.GetStockInfoByTicker;
import io.modicon.userservice.query.GetStockInfoByTickerResult;
import io.modicon.userservice.query.SearchStockInfoByTicker;
import io.modicon.userservice.query.SearchStockInfoByTickerResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface StockSearchOperation {
        @PostMapping
        GetStockInfoByTickerResult getStockByTicker(@RequestBody GetStockInfoByTicker query);

        @PostMapping("/search")
        SearchStockInfoByTickerResult searchStocksTicker(@RequestBody SearchStockInfoByTicker query);
}
