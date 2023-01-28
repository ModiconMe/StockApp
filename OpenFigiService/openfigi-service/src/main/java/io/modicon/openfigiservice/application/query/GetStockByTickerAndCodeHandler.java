package io.modicon.openfigiservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCodeResult;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerRequest;
import io.modicon.openfigiservice.application.service.OpenFigiStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetStockByTickerAndCodeHandler implements QueryHandler<GetStockByTickerAndCodeResult, GetStockByTickerAndCode> {

    private final OpenFigiStockService openFigiStockService;

    @Override
    public GetStockByTickerAndCodeResult handle(GetStockByTickerAndCode query) {
        List<GetTickerRequest> tickers = query.getStocks().stream()
                .map(s -> new GetTickerRequest("TICKER", s.ticker(), s.exchCode()))
                .collect(Collectors.toList());

        List<FoundedStockDto> currentCurrencyRate = openFigiStockService
                .getStockByTicker(tickers);

        return new GetStockByTickerAndCodeResult(currentCurrencyRate);
    }
}
