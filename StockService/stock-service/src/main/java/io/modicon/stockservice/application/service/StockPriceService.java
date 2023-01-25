package io.modicon.stockservice.application.service;

import io.modicon.stockservice.api.dto.StockWithPriceDto;

import java.util.List;

public interface StockPriceService {
    List<StockWithPriceDto> getStocksWithPrices(List<String> figis);
}
