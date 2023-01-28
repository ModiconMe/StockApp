package io.modicon.openfigiservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.openfigiservice.api.operation.OpenFigiOperation;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCodeResult;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCodeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/stocks")
public class OpenFigiController implements OpenFigiOperation {

    private final Bus bus;

    @Override
    public GetStockByTickerAndCodeResult getStock(GetStockByTickerAndCode query) {
        return bus.executeQuery(query);
    }

    @Override
    public SearchStockByTickerAndCodeResult searchStock(SearchStockByTickerAndCode query) {
        return bus.executeQuery(query);
    }
}
