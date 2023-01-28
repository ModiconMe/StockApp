package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCode;
import io.modicon.priceservice.api.query.GetStocksInfoFromRedis;
import io.modicon.priceservice.api.query.PutStocksInfoToRedis;
import io.modicon.userservice.application.client.OpenFigiServiceClient;
import io.modicon.userservice.application.client.StockInfoServiceClient;
import io.modicon.userservice.query.SearchStockInfoByTicker;
import io.modicon.userservice.query.SearchStockInfoByTickerResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchStockInfoByTickerHandler implements QueryHandler<SearchStockInfoByTickerResult, SearchStockInfoByTicker> {

    private final OpenFigiServiceClient openFigiServiceClient;
    private final StockInfoServiceClient stockInfoServiceClient;

    @Override
    public SearchStockInfoByTickerResult handle(SearchStockInfoByTicker query) {
        SearchStockDto stock = query.getStock();

        if (stock != null) {
            log.info("search stocks info in redis {}", stock);
            List<FoundedStockDto> stocksFromRedis = stockInfoServiceClient
                    .getStockInfoFromCache(new GetStocksInfoFromRedis(List.of(stock))).getStocks();
            if (!stocksFromRedis.isEmpty()) {
                log.info("successfully founded stocks in redis {}", stocksFromRedis);
                return new SearchStockInfoByTickerResult(stocksFromRedis);
            }
        }

        if (stock != null) {
            log.info("search stocks info in OpenFigi {}", stock);
            List<FoundedStockDto> stocksFromOpenFigi;
            var response = openFigiServiceClient
                    .searchStock(new SearchStockByTickerAndCode(stock)).getStocks();

            if (response != null)
                stocksFromOpenFigi = new ArrayList<>(response);
            else
                throw exception(HttpStatus.NOT_FOUND, "ticker not found", stock);

            if (!stocksFromOpenFigi.isEmpty()) {
                log.info("successfully founded stocks in OpenFigi {}", stocksFromOpenFigi);
                stockInfoServiceClient.putStockInfoToCache(new PutStocksInfoToRedis(stocksFromOpenFigi));
                return new SearchStockInfoByTickerResult(stocksFromOpenFigi);
            }
        }

        throw exception(HttpStatus.NOT_FOUND, "stock with ticker %s not found", stock.ticker());
    }
}
