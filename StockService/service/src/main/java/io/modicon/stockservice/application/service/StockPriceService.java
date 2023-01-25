package io.modicon.stockservice.application.service;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.client.ApiClientService;

import java.util.List;

public interface StockPriceService {
    List<StockWithPriceDto> getStocksWithPrices(ApiClientService apiClientService, List<String> figis);
}
