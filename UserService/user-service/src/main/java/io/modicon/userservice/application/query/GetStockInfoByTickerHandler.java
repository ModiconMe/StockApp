package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedis;
import io.modicon.priceservice.api.query.PutStocksInfoToRedis;
import io.modicon.userservice.application.client.OpenFigiServiceClient;
import io.modicon.userservice.application.client.StockInfoServiceClient;
import io.modicon.userservice.query.GetStockInfoByTicker;
import io.modicon.userservice.query.GetStockInfoByTickerResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStockInfoByTickerHandler implements QueryHandler<GetStockInfoByTickerResult, GetStockInfoByTicker> {

    private final OpenFigiServiceClient openFigiServiceClient;
    private final StockInfoServiceClient stockInfoServiceClient;

    @Override
    public GetStockInfoByTickerResult handle(GetStockInfoByTicker query) {
        List<FoundedStockDto> result = new ArrayList<>();
        Set<String> tickers = query.getStocks().stream().map(SearchStockDto::ticker).collect(Collectors.toSet());

        if (!tickers.isEmpty()) {
            log.info("try to get stocks info from redis {}", tickers);
            List<FoundedStockDto> stocksFromRedis = stockInfoServiceClient
                    .getStockInfoFromCache(new GetStocksInfoFromRedis(query.getStocks())).getStocks();
            result.addAll(stocksFromRedis);
            Set<String> tickersFromRedis = stocksFromRedis.stream()
                    .map(FoundedStockDto::ticker).collect(Collectors.toSet());
            tickers.removeAll(tickersFromRedis);
            log.info("successfully obtained stocks info from redis {}", tickersFromRedis);
        }

        if (!tickers.isEmpty()) {
            log.info("try to get stocks info from open figi {}", tickers);
            List<FoundedStockDto> stocksFromOpenFigi;
            var response = openFigiServiceClient.getStock(new GetStockByTickerAndCode(new HashSet<>(query.getStocks()))).getStocks();

            if (response != null)
                stocksFromOpenFigi = new ArrayList<>(response);
            else
                throw exception(HttpStatus.NOT_FOUND, "ticker not found", tickers);

            stockInfoServiceClient.putStockInfoToCache(new PutStocksInfoToRedis(stocksFromOpenFigi));
            result.addAll(stocksFromOpenFigi);
            Set<String> tickersFromOpenFigi = stocksFromOpenFigi.stream()
                    .map(FoundedStockDto::ticker).collect(Collectors.toSet());
            tickers.removeAll(tickersFromOpenFigi);
            log.info("successfully obtained stocks info from OpenFigi {}", tickersFromOpenFigi);
        }

        return new GetStockInfoByTickerResult(result);
    }
}
