package io.modicon.priceservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.priceservice.api.operation.PriceServiceOperation;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedis;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedisResult;
import io.modicon.priceservice.api.query.PutStocksWithPricesToRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/prices")
public class PriceServiceController implements PriceServiceOperation {

    private final Bus bus;

    @Override
    public GetStocksWithPricesFromRedisResult getPricesFromCache(GetStocksWithPricesFromRedis query) {
        return bus.executeQuery(query);
    }

    @Override
    public void putPricesToCache(PutStocksWithPricesToRedis command) {
        bus.executeCommand(command);
    }
}
