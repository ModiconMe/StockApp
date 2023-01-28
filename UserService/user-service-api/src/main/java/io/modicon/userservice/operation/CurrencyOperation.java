package io.modicon.userservice.operation;

import io.modicon.userservice.query.GetCurrencyRateDaySpecifiedResult;
import io.modicon.userservice.query.GetCurrentCurrencyRateResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CurrencyOperation {
        @GetMapping
        GetCurrentCurrencyRateResult getCurrentCurrencyRate();

        @GetMapping("/{date}")
        GetCurrencyRateDaySpecifiedResult getCurrencyRateDaySpecified(@PathVariable String date);
}
