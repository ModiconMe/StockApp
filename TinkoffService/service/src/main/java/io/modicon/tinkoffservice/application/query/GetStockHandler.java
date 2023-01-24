package io.modicon.tinkoffservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.tinkoffservice.api.query.GetStock;
import io.modicon.tinkoffservice.api.query.GetStockResult;
import io.modicon.tinkoffservice.application.StockMapper;
import io.modicon.tinkoffservice.application.service.StockService;
import io.modicon.tinkoffservice.infrastructure.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Instrument;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStockHandler implements QueryHandler<GetStockResult, GetStock> {

    private final StockService stockService;

    @Override
    public GetStockResult handle(GetStock query) {
        String figi = query.getFigi();
        CompletableFuture<Instrument> cf = stockService.getMarketInstrumentByFigi(figi);

        Optional<Instrument> optionalInstrument = Optional.ofNullable(cf.join());
        if (optionalInstrument.isEmpty()) throw ApiException.exception(HttpStatus.NOT_FOUND, "stock by figi:[%s] not found", figi);
        Instrument instrument = optionalInstrument.get();

        return new GetStockResult(StockMapper.mapToStock(instrument));
    }

}
