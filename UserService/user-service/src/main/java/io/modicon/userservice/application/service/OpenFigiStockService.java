package io.modicon.userservice.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCode;
import io.modicon.userservice.application.client.OpenFigiServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OpenFigiStockService {

    private final OpenFigiServiceClient openFigiServiceClient;

    public List<FoundedStockDto> getStocks(Set<SearchStockDto> requestedStocks) {
        return openFigiServiceClient.getStock(new GetStockByTickerAndCode(requestedStocks)).getStocks();
    }

    public List<FoundedStockDto> searchStocks(SearchStockDto requestedStock) {
        return openFigiServiceClient.searchStock(new SearchStockByTickerAndCode(requestedStock)).getStocks();
    }

}
