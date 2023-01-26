package io.modicon.priceservice.application.service;

import io.modicon.priceservice.application.StockMapper;
import io.modicon.priceservice.domain.model.StockWithPrice;
import io.modicon.priceservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PriceSetterService {

    private final StockRepository stockRepository;
    private final TinkoffStockPriceService tinkoffStockPriceService;

    public void updatePrices() {
        log.info("update prices");
        List<StockWithPrice> stocks = new ArrayList<>();

        stockRepository.findAll().forEach(i -> {
            if(Objects.nonNull(i)) {
                stocks.add(i);
            }
        });

        Set<String> figis = stocks.stream().map(StockWithPrice::figi).collect(Collectors.toSet());

        if (!stocks.isEmpty()) {
            Map<String, BigDecimal> stockPrices = tinkoffStockPriceService.getNewPrices(figis);
            Set<StockWithPrice> updatedStocks = stocks.stream().map(stock -> StockMapper.updatePrice(stock, stockPrices.get(stock.figi())))
                    .collect(Collectors.toSet());
            figis.removeAll(stockPrices.keySet());
            stockRepository.saveAll(updatedStocks);
        }
    }

}
