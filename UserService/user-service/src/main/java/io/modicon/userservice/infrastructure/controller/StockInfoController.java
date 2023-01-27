package io.modicon.userservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.userservice.operation.StockSearchOperation;
import io.modicon.userservice.query.GetStockInfoByTicker;
import io.modicon.userservice.query.GetStockInfoByTickerResult;
import io.modicon.userservice.query.SearchStockInfoByTicker;
import io.modicon.userservice.query.SearchStockInfoByTickerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks")
public class StockInfoController implements StockSearchOperation {

    private final Bus bus;

    @Override
    public GetStockInfoByTickerResult getStockByTicker(GetStockInfoByTicker query) {
        return bus.executeQuery(query);
    }

    @Override
    public SearchStockInfoByTickerResult getCurrencyRateDaySpecified(SearchStockInfoByTicker query) {
        return bus.executeQuery(query);
    }
}
