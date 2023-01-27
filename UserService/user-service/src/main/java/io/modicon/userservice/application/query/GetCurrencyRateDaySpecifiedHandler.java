package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.client.CurrencyServiceClient;
import io.modicon.userservice.query.GetCurrencyRateDaySpecified;
import io.modicon.userservice.query.GetCurrencyRateDaySpecifiedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetCurrencyRateDaySpecifiedHandler implements QueryHandler<GetCurrencyRateDaySpecifiedResult, GetCurrencyRateDaySpecified> {

    private final CurrencyServiceClient currencyServiceClient;

    @Override
    public GetCurrencyRateDaySpecifiedResult handle(GetCurrencyRateDaySpecified query) {
        return new GetCurrencyRateDaySpecifiedResult(currencyServiceClient
                .getCurrencyRateDaySpecified(query.getDate()).getCurrencyRates());
    }

}
