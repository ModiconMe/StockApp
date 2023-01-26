package io.modicon.stockservice.infrastructure.controller;

import io.modicon.stockservice.api.operation.StockBondOperation;
import io.modicon.stockservice.api.query.GetStocks;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.cqrsbus.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks")
public class StockBondController implements StockBondOperation {

    private final Bus bus;

    @Override
    public GetStocksResult getStocks(GetStocks query) {
        return bus.executeQuery(query);
    }

    @Override
    public GetStocksWithPricesResult getStockWithPrices(GetStocksWithPrices query) {
        return bus.executeQuery(query);
    }
}
