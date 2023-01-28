package io.modicon.cbrservice.infrastructure.controller;

import io.modicon.cbrservice.api.operation.CbrCurrencyOperation;
import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecified;
import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecifiedResult;
import io.modicon.cbrservice.api.query.GetCurrentCurrencyRate;
import io.modicon.cbrservice.api.query.GetCurrentCurrencyRateResult;
import io.modicon.cqrsbus.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/currency")
public class CbrCurrencyController implements CbrCurrencyOperation {

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
