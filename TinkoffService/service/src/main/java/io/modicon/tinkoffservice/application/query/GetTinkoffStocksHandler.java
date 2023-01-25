package io.modicon.tinkoffservice.application.query;

import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.cqrsbus.QueryHandler;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocks;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocksResult;
import io.modicon.tinkoffservice.application.StockMapper;
import io.modicon.tinkoffservice.application.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Instrument;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetTinkoffStocksHandler implements QueryHandler<GetTinkoffStocksResult, GetTinkoffStocks> {

    private final StockService stockService;

    @Override
    public GetTinkoffStocksResult handle(GetTinkoffStocks query) {
        List<String> figis = query.getFigis();
        List<CompletableFuture<Instrument>> instrumentsCF = figis
                .stream().map(stockService::getMarketInstrumentByFigi).toList();

        List<StockDto> stocks = instrumentsCF.stream()
                .map((cf) -> cf.handle((s, t) -> t != null ? null : s))
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .map(StockMapper::mapToDto).toList();

        figis.removeAll(stocks.stream().map(StockDto::figi).toList());
        return new GetTinkoffStocksResult(stocks, figis);
    }

}
