package io.modicon.tinkoffservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.tinkoffservice.api.dto.StockDto;
import io.modicon.tinkoffservice.api.query.GetStocks;
import io.modicon.tinkoffservice.api.query.GetStocksResult;
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
public class GetStocksHandler implements QueryHandler<GetStocksResult, GetStocks> {

    private final StockService stockService;

    @Override
    public GetStocksResult handle(GetStocks query) {
        List<CompletableFuture<Instrument>> instrumentsCF = query.getFigis()
                .stream().map(stockService::getMarketInstrumentByFigi).toList();

        List<StockDto> stocks = instrumentsCF.stream()
                .map((cf) -> cf.handle((s, t) -> t != null ? null : s))
                .map(CompletableFuture::join)
                .filter((Objects::nonNull))
                .map(StockMapper::mapToDto).toList();

        return new GetStocksResult(stocks);
    }

}
