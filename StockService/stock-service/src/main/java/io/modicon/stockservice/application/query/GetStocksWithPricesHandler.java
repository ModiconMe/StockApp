package io.modicon.stockservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.priceservice.api.query.GetStocksWithPricesFromRedis;
import io.modicon.priceservice.api.query.PutStocksWithPricesToRedis;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.stockservice.application.client.PriceServiceClient;
import io.modicon.stockservice.application.service.MoexStockPriceService;
import io.modicon.stockservice.application.service.TinkoffStockPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStocksWithPricesHandler implements QueryHandler<GetStocksWithPricesResult, GetStocksWithPrices> {

    private final TinkoffStockPriceService tinkoffStockPriceService;
    private final MoexStockPriceService moexStockPriceService;
    private final PriceServiceClient priceServiceClient;

    @Override
    public GetStocksWithPricesResult handle(GetStocksWithPrices query) {

        List<String> figis = new ArrayList<>(query.getFigis().stream().map(String::trim).toList());
        List<StockWithPriceDto> resultList = new ArrayList<>();

        if (!figis.isEmpty()) {
            log.info("get stocks from redis-service");
            Set<StockWithPriceDto> stocksFromPriceService = priceServiceClient.getPricesFromCache(new GetStocksWithPricesFromRedis(figis)).getStockPrices();
            List<String> figisFromPriceService = stocksFromPriceService.stream().map(StockWithPriceDto::figi).toList();
            figis.removeAll(figisFromPriceService);
            resultList.addAll(stocksFromPriceService);
            log.info("successfully received stocks with prices from price-service - {}", stocksFromPriceService);
        }

        if (!figis.isEmpty()) {
            log.info("get stocks from tinkoff-service");
            Set<StockWithPriceDto> stocksFromTinkoff = tinkoffStockPriceService.getStocksWithPrices(figis);
            resultList.addAll(stocksFromTinkoff);
            log.info("successfully received stocks with prices from tinkoff-service - {}", stocksFromTinkoff);
            priceServiceClient.putPricesToCache(new PutStocksWithPricesToRedis(new ArrayList<>(stocksFromTinkoff)));
            log.info("saved stocks in price-server: {}", stocksFromTinkoff);
        }

        if (!figis.isEmpty()) {
            log.info("get stocks from moex-service");
            Set<StockWithPriceDto> stocksFromMoex = moexStockPriceService.getStocksWithPrices(figis);
            resultList.addAll(stocksFromMoex);
            log.info("successfully received stocks with prices from moex-service - {}", stocksFromMoex);
        }

        return new GetStocksWithPricesResult(resultList, new HashSet<>(figis));
    }

}
