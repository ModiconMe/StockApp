package io.modicon.tinkoffservice.application.service;

import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.Instrument;

import java.util.concurrent.CompletableFuture;

public interface StockService {
    CompletableFuture<Instrument> getMarketInstrumentByFigi(String figi);

    CompletableFuture<Instrument> getMarketInstrumentByTicker(String ticker);

    CompletableFuture<GetOrderBookResponse> getOrderBookByFigi(String figi);
}
