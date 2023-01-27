package io.modicon.cbrservice.api.operation;

import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecifiedResult;
import io.modicon.cbrservice.api.query.GetCurrentCurrencyRateResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CbrCurrencyOperation {

    @GetMapping
    GetCurrentCurrencyRateResult getCurrentCurrencyRate();

    @GetMapping("/{date}")
    GetCurrencyRateDaySpecifiedResult getCurrencyRateDaySpecified(@PathVariable String date);
}
