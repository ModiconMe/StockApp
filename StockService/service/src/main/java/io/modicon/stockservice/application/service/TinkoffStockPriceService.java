package io.modicon.stockservice.application.service;

import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.ApiClientService;
import io.modicon.stockservice.model.Stock;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPrices;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TinkoffStockPriceService implements StockPriceService {

    @Override
    public List<StockWithPriceDto> getStocksWithPrices(ApiClientService apiClientService, List<String> figis) {
        List<Stock> tinkoffStocks = apiClientService.tinkoffService().getStocks(new GetTinkoffStocks()).getStocks()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromTinkoff = tinkoffStocks.stream().map(Stock::figi).toList();
        figis.removeAll(figisFromTinkoff);
        List<StockPriceDto> tinkoffStockPrices = apiClientService.tinkoffService().getPrices(new GetTinkoffStockPrices(figisFromTinkoff))
                .getStockPrices();
        Map<String, BigDecimal> tinkoffFigisPrices = tinkoffStockPrices.stream()
                .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
        return tinkoffStocks.stream()
                .map(s -> StockMapper.mapToStockWithPricesDto(s, tinkoffFigisPrices.get(s.figi()))).toList();
    }

}
