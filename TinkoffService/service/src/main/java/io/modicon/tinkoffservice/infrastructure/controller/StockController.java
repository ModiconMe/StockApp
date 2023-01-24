package io.modicon.tinkoffservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.tinkoffservice.api.operation.StockOperation;
import io.modicon.tinkoffservice.api.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/")
public class StockController implements StockOperation {

    private final Bus bus;

    @Override
    public GetStockResult getStock(String figi) {
        return bus.executeQuery(new GetStock(figi));
    }

    @Override
    public GetStocksResult getStocks(GetStocks getStocks) {
        return bus.executeQuery(getStocks);
    }

    @Override
    public GetStockPricesResult getPrices(GetStockPrices getStockPrices) {
        return bus.executeQuery(getStockPrices);
    }
}
