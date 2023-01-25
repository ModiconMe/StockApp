package io.modicon.stockservice.application.query;

import io.modicon.cqrsbus.QueryHandler;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.stockservice.application.client.ApiClientService;
import io.modicon.stockservice.application.service.MoexStockPriceService;
import io.modicon.stockservice.application.service.StockPriceService;
import io.modicon.stockservice.application.service.TinkoffStockPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.modicon.stockservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStocksWithPricesHandler implements QueryHandler<GetStocksWithPricesResult, GetStocksWithPrices> {

    private final ApiClientService clientService;
    private final TinkoffStockPriceService tinkoffStockPriceService;
    private final MoexStockPriceService moexStockPriceService;

    @Override
    public GetStocksWithPricesResult handle(GetStocksWithPrices query) {

        List<String> figis = new ArrayList<>(query.getFigis().stream().map(String::trim).toList());
        List<StockWithPriceDto> resultList = new ArrayList<>();

        if (!figis.isEmpty()) {
            log.info("get stocks from tinkoff-service");
            List<StockWithPriceDto> stocksFromTinkoff = tinkoffStockPriceService.getStocksWithPrices(clientService, figis);
            resultList.addAll(stocksFromTinkoff);
            log.info("successfully received stocks with prices from tinkoff-service - {}", stocksFromTinkoff);
        }

        if (!figis.isEmpty()) {
            log.info("get stocks from moex-service");
            List<StockWithPriceDto> stocksFromMoex = moexStockPriceService.getStocksWithPrices(clientService, figis);
            resultList.addAll(stocksFromMoex);
            log.info("successfully received stocks with prices from moex-service - {}", stocksFromMoex);
        }

        return new GetStocksWithPricesResult(resultList, figis.stream().distinct().toList());
    }

}
