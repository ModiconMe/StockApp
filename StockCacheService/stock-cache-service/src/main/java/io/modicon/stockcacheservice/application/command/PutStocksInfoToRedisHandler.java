package io.modicon.stockcacheservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.priceservice.api.query.PutStocksInfoToRedis;
import io.modicon.priceservice.api.query.PutStocksInfoToRedisResult;
import io.modicon.stockcacheservice.application.StockMapper;
import io.modicon.stockcacheservice.domain.model.StockInfo;
import io.modicon.stockcacheservice.domain.repository.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PutStocksInfoToRedisHandler implements CommandHandler<PutStocksInfoToRedisResult, PutStocksInfoToRedis> {

    private final StockInfoRepository stockInfoRepository;

    @Override
    public PutStocksInfoToRedisResult handle(PutStocksInfoToRedis cmd) {
        List<StockInfo> stocks = cmd.getStocks().stream()
                .map(StockMapper::mapToFoundedStock).toList();

        stockInfoRepository.saveAll(stocks);

        return new PutStocksInfoToRedisResult();
    }
}
