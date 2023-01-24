package io.modicon.tinkoffservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.tinkoffservice.api.operation.TinkoffStockOperation;
import io.modicon.tinkoffservice.api.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks/")
public class StockController implements TinkoffStockOperation {

    private final Bus bus;

    @Override
    public GetStockResult getStock(String figi) {
        return bus.executeQuery(new GetStock(figi));
    }

    @Override
    public GetStocksResult getStocks(GetStocks query) {
        return bus.executeQuery(query);
    }

    @Override
    public GetStockPricesResult getPrices(GetStockPrices query) {
        return bus.executeQuery(query);
    }
}
