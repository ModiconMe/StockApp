package io.modicon.tinkoffservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {

    private final InvestApi investApi;

    @Async
    public CompletableFuture<Instrument> getMarketInstrumentByFigi(String figi) {
        var instrumentsService = investApi.getInstrumentsService();
        return instrumentsService.getInstrumentByFigi(figi);
    }

    @Async
    public CompletableFuture<GetOrderBookResponse> getOrderBookByFigi(String figi) {
        return investApi.getMarketDataService().getOrderBook(figi, 1);
    }

//    private Double toDouble(long val1, long val2) {
//        String units = String.valueOf(val1);
//        String nano = String.valueOf(val2);
//        return Double.parseDouble(units + "." + nano);
//    }

//    @Override
//    public StocksPricesDto getPrices(FigisDto figisDto) {
//        var cfs = figisDto.figis().stream().map(this::getOrderBookByFigi).toList();
//        return new StocksPricesDto(cfs.stream()
//                .map((cf) -> cf.handle((s, t) -> t != null ? null : s))
//                .map(CompletableFuture::join)
//                .filter((Objects::nonNull))
//                .map((q) -> new StockPrice(q.getFigi(), toDouble(q.getLastPrice().getUnits(), q.getLastPrice().getNano())))
//                .toList());
//    }
}
