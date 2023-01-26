package io.modicon.stockservice.application.service;

import io.modicon.stockservice.api.dto.StockWithPriceDto;

import java.util.List;
import java.util.Set;

public interface StockPriceService {
    Set<StockWithPriceDto> getStocksWithPrices(List<String> figis);
}
