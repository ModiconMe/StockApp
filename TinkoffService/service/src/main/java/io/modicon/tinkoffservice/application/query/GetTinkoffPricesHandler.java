package io.modicon.tinkoffservice.application.query;

import com.google.common.base.Strings;
import io.modicon.cqrsbus.QueryHandler;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPrices;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPricesResult;
import io.modicon.tinkoffservice.application.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetTinkoffPricesHandler implements QueryHandler<GetTinkoffStockPricesResult, GetTinkoffStockPrices> {

    private final StockService stockService;

    @Override
    public GetTinkoffStockPricesResult handle(GetTinkoffStockPrices query) {
        List<String> figis = query.getFigis();
        var cfs = figis.stream().map(stockService::getOrderBookByFigi).toList();
        List<StockPriceDto> stockPrices = cfs.stream()
                .map(cf -> cf.handle((s, t) -> t != null ? null : s))
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .filter(s -> !Strings.isNullOrEmpty(s.getFigi()))
                .map((q) -> new StockPriceDto(q.getFigi(), toBigDecimal(q.getLastPrice().getUnits(), q.getLastPrice().getNano())))
                .toList();

        return new GetTinkoffStockPricesResult(stockPrices);
    }

    private BigDecimal toBigDecimal(long val1, long val2) {
        String units = String.valueOf(val1);
        String nano = String.valueOf(val2);
        return BigDecimal.valueOf(Double.parseDouble(units + "." + nano));
    }

}
