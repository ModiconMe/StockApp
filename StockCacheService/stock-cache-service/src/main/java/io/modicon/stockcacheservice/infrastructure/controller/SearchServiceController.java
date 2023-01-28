package io.modicon.stockcacheservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.priceservice.api.operation.SearchServiceOperation;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedis;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedisResult;
import io.modicon.priceservice.api.query.PutStocksInfoToRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks")
public class SearchServiceController implements SearchServiceOperation {

    private final Bus bus;

    @Override
    public GetStocksInfoFromRedisResult getStockInfoFromCache(GetStocksInfoFromRedis query) {
        return bus.executeQuery(query);
    }

    @Override
    public void putStockInfoToCache(PutStocksInfoToRedis command) {
        bus.executeCommand(command);
    }
}
