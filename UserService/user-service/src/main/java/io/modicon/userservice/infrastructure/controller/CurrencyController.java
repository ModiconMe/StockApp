package io.modicon.userservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.userservice.operation.CurrencyOperation;
import io.modicon.userservice.query.GetCurrencyRateDaySpecified;
import io.modicon.userservice.query.GetCurrencyRateDaySpecifiedResult;
import io.modicon.userservice.query.GetCurrentCurrencyRate;
import io.modicon.userservice.query.GetCurrentCurrencyRateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/currency")
public class CurrencyController implements CurrencyOperation {

    private final Bus bus;

    @Override
    public GetCurrentCurrencyRateResult getCurrentCurrencyRate() {
        return bus.executeQuery(new GetCurrentCurrencyRate());
    }

    @Override
    public GetCurrencyRateDaySpecifiedResult getCurrencyRateDaySpecified(String date) {
        return bus.executeQuery(new GetCurrencyRateDaySpecified(date));
    }
}
