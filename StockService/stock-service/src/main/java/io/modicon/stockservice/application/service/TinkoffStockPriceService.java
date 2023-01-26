package io.modicon.stockservice.application.service;

import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.TinkoffServiceClient;
import io.modicon.stockservice.model.Stock;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPrices;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TinkoffStockPriceService implements StockPriceService {

    private final TinkoffServiceClient tinkoffServiceClient;

    @Override
    public Set<StockWithPriceDto> getStocksWithPrices(List<String> figis) {
        List<Stock> tinkoffStocks = tinkoffServiceClient.getStocks(new GetTinkoffStocks(figis)).getStocks()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromTinkoff = tinkoffStocks.stream().map(Stock::figi).toList();
        figis.removeAll(figisFromTinkoff);
        List<StockPriceDto> tinkoffStockPrices = tinkoffServiceClient.getPrices(new GetTinkoffStockPrices(figisFromTinkoff))
                .getStockPrices();
        Map<String, BigDecimal> tinkoffFigisPrices = tinkoffStockPrices.stream()
                .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
        return tinkoffStocks.stream()
                .map(s -> StockMapper.mapToStockWithPricesDto(s, tinkoffFigisPrices.get(s.figi()))).collect(Collectors.toSet());
    }

}
