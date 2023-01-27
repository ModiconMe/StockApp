package io.modicon.stockcacheservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.priceservice.api.query.PutStocksWithPricesToRedis;
import io.modicon.priceservice.api.query.PutStocksWithPricesToRedisResult;
import io.modicon.stockcacheservice.application.StockMapper;
import io.modicon.stockcacheservice.domain.model.StockWithPrice;
import io.modicon.stockcacheservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PutStocksWithPricesToRedisHandler implements CommandHandler<PutStocksWithPricesToRedisResult, PutStocksWithPricesToRedis> {

    private final StockRepository stockRepository;

    @Override
    public PutStocksWithPricesToRedisResult handle(PutStocksWithPricesToRedis cmd) {
        Set<StockWithPrice> stocks = cmd.getStocks().stream()
                .map(StockMapper::mapToStockWithPrice)
                .collect(Collectors.toSet());

        stockRepository.saveAll(stocks);

        return new PutStocksWithPricesToRedisResult();
    }
}
