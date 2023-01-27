package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.client.CurrencyServiceClient;
import io.modicon.userservice.query.GetCurrentCurrencyRate;
import io.modicon.userservice.query.GetCurrentCurrencyRateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetCurrentCurrencyRateHandler implements QueryHandler<GetCurrentCurrencyRateResult, GetCurrentCurrencyRate> {

    private final CurrencyServiceClient currencyServiceClient;

    @Override
    public GetCurrentCurrencyRateResult handle(GetCurrentCurrencyRate query) {
        return new GetCurrentCurrencyRateResult(currencyServiceClient.getCurrentCurrencyRate().getCurrencyRates());
    }

}
