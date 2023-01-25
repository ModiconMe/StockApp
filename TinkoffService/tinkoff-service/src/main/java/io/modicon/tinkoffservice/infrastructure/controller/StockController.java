package io.modicon.tinkoffservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.tinkoffservice.api.operation.TinkoffServiceOperation;
import io.modicon.tinkoffservice.api.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks")
public class StockController implements TinkoffServiceOperation {

    private final Bus bus;

    @Override
    public GetTinkoffStockResult getStock(String figi) {
        return bus.executeQuery(new GetTinkoffStock(figi));
    }

    @Override
    public GetTinkoffStocksResult getStocks(GetTinkoffStocks query) {
        return bus.executeQuery(query);
    }

    @Override
    public GetTinkoffStockPricesResult getPrices(GetTinkoffStockPrices query) {
        return bus.executeQuery(query);
    }
}
