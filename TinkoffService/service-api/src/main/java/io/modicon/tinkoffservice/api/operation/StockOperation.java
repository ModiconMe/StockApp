package io.modicon.tinkoffservice.api.operation;

import io.modicon.tinkoffservice.api.query.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface StockOperation {

    @GetMapping("/{figi}")
    GetStockResult getStock(@PathVariable("figi") String figi);

    @PostMapping
    GetStocksResult getStocks(@RequestBody GetStocks getStocks);

    @PostMapping("/prices")
    GetStockPricesResult getPrices(@RequestBody GetStockPrices getStockPrices);

}
