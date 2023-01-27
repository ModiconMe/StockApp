package io.modicon.openfigiservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCodeResult;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerRequest;
import io.modicon.openfigiservice.application.service.OpenFigiStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchStockByTickerAndCodeHandler implements QueryHandler<SearchStockByTickerAndCodeResult, SearchStockByTickerAndCode> {

    private final OpenFigiStockService openFigiStockService;

    @Override
    public SearchStockByTickerAndCodeResult handle(SearchStockByTickerAndCode query) {

        SearchStockDto searchedStock = query.getStock();
        List<FoundedStockDto> foundedStockDtos = openFigiStockService
                .searchStockByTicker(new SearchTickerRequest(searchedStock.ticker(), searchedStock.exchCode()));

        return new SearchStockByTickerAndCodeResult(foundedStockDtos);
    }
}
