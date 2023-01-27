package io.modicon.stockcacheservice.application.service;

import io.modicon.stockcacheservice.application.client.TinkoffServiceClient;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPrices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TinkoffStockPriceService implements StockPriceService {

    private final TinkoffServiceClient tinkoffServiceClient;

    @Override
    public Map<String, BigDecimal> getNewPrices(Set<String> figis) {
        return tinkoffServiceClient
                .getPrices(new GetTinkoffStockPrices(new ArrayList<>(figis))).getStockPrices()
                .stream().collect(Collectors.toMap(StockPriceDto::figi, StockPriceDto::price));
    }
}
