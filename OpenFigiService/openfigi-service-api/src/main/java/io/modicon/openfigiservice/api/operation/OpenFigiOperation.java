package io.modicon.openfigiservice.api.operation;

import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCodeResult;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCode;
import io.modicon.openfigiservice.api.query.SearchStockByTickerAndCodeResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OpenFigiOperation {

    @PostMapping
    GetStockByTickerAndCodeResult getStock(@RequestBody GetStockByTickerAndCode query);

    @PostMapping("/search")
    SearchStockByTickerAndCodeResult searchStock(@RequestBody SearchStockByTickerAndCode query);

}
