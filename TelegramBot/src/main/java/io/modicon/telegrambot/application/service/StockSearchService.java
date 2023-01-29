package io.modicon.telegrambot.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.telegrambot.application.client.StockSearchOperationsClient;
import io.modicon.userservice.query.SearchStockInfoByTicker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StockSearchService {

    private final StockSearchOperationsClient stockSearchOperationsClient;

    public Set<FoundedStockDto> searchStockByTickerAndExchangeCode(String ticker) {
        return new HashSet<>(stockSearchOperationsClient.searchStocksTicker(new SearchStockInfoByTicker(new SearchStockDto(ticker, "RX"))).getStocks());
    }

}
