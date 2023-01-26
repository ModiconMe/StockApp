package io.modicon.stockservice.application.service;

import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.MoexServiceClient;
import io.modicon.stockservice.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MoexStockPriceService implements StockPriceService {

    private final MoexServiceClient moexServiceClient;

    @Override
    public Set<StockWithPriceDto> getStocksWithPrices(List<String> figis) {
        List<Stock> moexStocks = moexServiceClient.getBonds(new GetMoexBonds(figis)).getBonds()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromMoex = moexStocks.stream().map(Stock::figi).toList();
        figis.removeAll(figisFromMoex);
        List<StockPriceDto> moexStockPrices = moexServiceClient.getPricesByFigis(new GetMoexBondPrices(figisFromMoex))
                .getBondPrices();
        Map<String, BigDecimal> moexFigisPrices = moexStockPrices.stream()
                .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
        Set<StockWithPriceDto> stockWithPriceDtos = moexStocks.stream()
                .map(s -> StockMapper.mapToStockWithPricesDto(s, moexFigisPrices.get(s.figi()))).collect(Collectors.toSet());
        return stockWithPriceDtos;
    }

}
