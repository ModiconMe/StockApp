package io.modicon.priceservice.api.operation;

import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedis;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedisResult;
import io.modicon.priceservice.api.query.PutStocksWithPricesToRedis;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PriceServiceOperation {

    @PostMapping("/get-cache")
    GetStocksWithPricesFromRedisResult getPricesFromCache(@RequestBody GetStocksWithPricesFromRedis query);

    @PostMapping("/update-cache")
    void putPricesToCache(@RequestBody PutStocksWithPricesToRedis command);

}
