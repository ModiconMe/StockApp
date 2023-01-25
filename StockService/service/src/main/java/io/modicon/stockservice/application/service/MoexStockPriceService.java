package io.modicon.stockservice.application.service;

import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.ApiClientService;
import io.modicon.stockservice.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MoexStockPriceService implements StockPriceService {

    @Override
    public List<StockWithPriceDto> getStocksWithPrices(ApiClientService apiClientService, List<String> figis) {
        List<Stock> moexStocks = apiClientService.moexService().getBonds(new GetMoexBonds(figis)).getBonds()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromMoex = moexStocks.stream().map(Stock::figi).toList();
        figis.removeAll(figisFromMoex);
        List<StockPriceDto> moexStockPrices = apiClientService.moexService().getPricesByFigis(new GetMoexBondPrices(figisFromMoex))
                .getBondPrices();
        Map<String, BigDecimal> moexFigisPrices = moexStockPrices.stream()
                .collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
        return moexStocks.stream()
                .map(s -> StockMapper.mapToStockWithPricesDto(s, moexFigisPrices.get(s.figi()))).toList();
    }

}
