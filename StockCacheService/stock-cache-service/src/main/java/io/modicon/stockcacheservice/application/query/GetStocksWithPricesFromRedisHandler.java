package io.modicon.stockcacheservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedis;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedisResult;
import io.modicon.stockcacheservice.application.StockMapper;
import io.modicon.stockcacheservice.domain.model.StockWithPrice;
import io.modicon.stockcacheservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetStocksWithPricesFromRedisHandler implements QueryHandler<GetStocksWithPricesFromRedisResult, GetStocksWithPricesFromRedis> {

    private final StockRepository stockRepository;

    @Override
    public GetStocksWithPricesFromRedisResult handle(GetStocksWithPricesFromRedis query) {
        Set<String> figis = new HashSet<>(query.getFigis());
        List<StockWithPrice> resultList = new ArrayList<>();
        figis.forEach(figi -> {
            resultList.add(stockRepository.findById(figi).orElse(null));
        });

        return new GetStocksWithPricesFromRedisResult(
                resultList.stream()
                .filter(Objects::nonNull)
                .map(StockMapper::mapToStockWithPricesDto)
                .collect(Collectors.toSet())
        );
    }
}
