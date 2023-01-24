package io.modicon.stockservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBonds;

import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.MoexServiceClient;
import io.modicon.stockservice.application.client.TinkoffServiceClient;
import io.modicon.stockservice.model.Stock;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPrices;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.modicon.stockservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStocksWithPricesHandler implements QueryHandler<GetStocksWithPricesResult, GetStocksWithPrices> {

    private final TinkoffServiceClient tinkoffService;
    private final MoexServiceClient moexService;

    @Override
    public GetStocksWithPricesResult handle(GetStocksWithPrices query) {

        List<String> figis = new ArrayList<>(query.getFigi());
        List<StockWithPriceDto> resultList;

        log.info("get stocks from tinkoff-service");
        List<Stock> tinkoffStocks = tinkoffService.getStocks(new GetTinkoffStocks()).getStocks()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromTinkoff = tinkoffStocks.stream().map(Stock::figi).toList();
        List<StockPriceDto> tinkoffStockPrices = tinkoffService.getPrices(new GetTinkoffStockPrices(figisFromTinkoff))
                .getStockPrices();
        Map<String, BigDecimal> tinkoffFigisPrices = tinkoffStockPrices.stream()
                .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
        List<StockWithPriceDto> tinkoffStockWithPrices = tinkoffStocks.stream()
                .map(s -> StockMapper.mapToStockWithPricesDto(s, tinkoffFigisPrices.get(s.figi()))).toList();
        figis.removeAll(figisFromTinkoff);
        log.info("successfully received stocks with prices from tinkoff-service - {}", figisFromTinkoff);
        resultList = new ArrayList<>(tinkoffStockWithPrices);

        if (!figis.isEmpty()) {
            log.info("get stocks from moex-service");
            List<Stock> moexStocks = moexService.getBonds(new GetMoexBonds(figis)).getBonds()
                    .stream().map(StockMapper::mapToStock).toList();
            List<String> figisFromMoex = moexStocks.stream().map(Stock::figi).toList();
            List<StockPriceDto> moexStockPrices = moexService.getPricesByFigis(new GetMoexBondPrices(figisFromMoex))
                    .getBondPrices();
            Map<String, BigDecimal> moexFigisPrices = moexStockPrices.stream()
                    .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
            List<StockWithPriceDto> moexStockWithPrices = tinkoffStocks.stream()
                    .map(s -> StockMapper.mapToStockWithPricesDto(s, moexFigisPrices.get(s.figi()))).toList();
            figis.removeAll(figisFromMoex);
            if (!figis.isEmpty())
                throw exception(HttpStatus.NOT_FOUND, "stocks %s not fount", figis);
            log.info("successfully received stocks with prices from moex-service - {}", figisFromTinkoff);
            resultList.addAll(moexStockWithPrices);
        }

        return new GetStocksWithPricesResult(resultList);
    }

}
