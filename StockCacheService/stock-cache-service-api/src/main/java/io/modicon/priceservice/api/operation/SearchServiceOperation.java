package io.modicon.priceservice.api.operation;

import io.modicon.priceservice.api.query.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SearchServiceOperation {

    @PostMapping("/get-stock-info-cache")
    GetStocksInfoFromRedisResult getStockInfoFromCache(@RequestBody GetStocksInfoFromRedis query);

    @PostMapping("/update-stock-info-cache")
    void putStockInfoToCache(@RequestBody PutStocksInfoToRedis command);

}
