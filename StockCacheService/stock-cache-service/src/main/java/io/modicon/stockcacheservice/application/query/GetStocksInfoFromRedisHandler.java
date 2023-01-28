package io.modicon.stockcacheservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedis;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedisResult;
import io.modicon.stockcacheservice.application.StockMapper;
import io.modicon.stockcacheservice.domain.model.StockInfo;
import io.modicon.stockcacheservice.domain.repository.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetStocksInfoFromRedisHandler implements QueryHandler<GetStocksInfoFromRedisResult, GetStocksInfoFromRedis> {
    
    private final StockInfoRepository stockInfoRepository;

    @Override
    public GetStocksInfoFromRedisResult handle(GetStocksInfoFromRedis query) {
        List<StockInfo> stocks = new ArrayList<>();
        Set<String> tickers = query.getStocks().stream().map(SearchStockDto::ticker).collect(Collectors.toSet());

        tickers.forEach(t -> {
            stocks.add(stockInfoRepository.findByTicker(t).orElse(null));
        });

        return new GetStocksInfoFromRedisResult(stocks.stream()
                .filter(Objects::nonNull)
                .map(StockMapper::mapToFoundedStockDto)
                .toList());
    }
}
