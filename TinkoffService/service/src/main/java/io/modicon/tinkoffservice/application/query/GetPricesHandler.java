package io.modicon.tinkoffservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.tinkoffservice.api.dto.StockDto;
import io.modicon.tinkoffservice.api.dto.StockPriceDto;
import io.modicon.tinkoffservice.api.query.GetStockPrices;
import io.modicon.tinkoffservice.api.query.GetStockPricesResult;
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
public class GetPricesHandler implements QueryHandler<GetStockPricesResult, GetStockPrices> {

    private final StockService stockService;

    @Override
    public GetStockPricesResult handle(GetStockPrices query) {
        List<String> figis = query.getFigis();
        var cfs = figis.stream().map(stockService::getOrderBookByFigi).toList();
        List<StockPriceDto> stockPrices = cfs.stream()
                .map((cf) -> cf.handle((s, t) -> t != null ? null : s))
                .map(CompletableFuture::join)
                .filter((Objects::nonNull))
                .map((q) -> new StockPriceDto(q.getFigi(), toDouble(q.getLastPrice().getUnits(), q.getLastPrice().getNano())))
                .toList();
        return new GetStockPricesResult(stockPrices);
    }

    private Double toDouble(long val1, long val2) {
        String units = String.valueOf(val1);
        String nano = String.valueOf(val2);
        return Double.parseDouble(units + "." + nano);
    }

}
