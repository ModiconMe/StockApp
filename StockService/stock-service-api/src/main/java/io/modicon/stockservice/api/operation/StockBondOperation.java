package io.modicon.stockservice.api.operation;

import io.modicon.stockservice.api.query.GetStocks;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface StockBondOperation {

    @PostMapping
    GetStocksResult getStocks(@RequestBody GetStocks query);

    @PostMapping("/prices")
    GetStocksWithPricesResult getStockWithPrices(@RequestBody GetStocksWithPrices query);
}
