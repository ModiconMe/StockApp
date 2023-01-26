package io.modicon.tinkoffservice.api.operation;

import io.modicon.tinkoffservice.api.query.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface TinkoffServiceOperation {

    @GetMapping("/{figi}")
    GetTinkoffStockResult getStock(@PathVariable("figi") String figi);

    @PostMapping
    GetTinkoffStocksResult getStocks(@RequestBody GetTinkoffStocks query);

    @PostMapping("/prices")
    GetTinkoffStockPricesResult getPrices(@RequestBody GetTinkoffStockPrices query);

}
