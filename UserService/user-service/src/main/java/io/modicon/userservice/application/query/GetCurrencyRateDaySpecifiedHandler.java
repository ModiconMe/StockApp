package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import io.modicon.userservice.application.client.CurrencyServiceClient;
import io.modicon.userservice.application.service.DateFormatService;
import io.modicon.userservice.query.GetCurrencyRateDaySpecified;
import io.modicon.userservice.query.GetCurrencyRateDaySpecifiedResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetCurrencyRateDaySpecifiedHandler implements QueryHandler<GetCurrencyRateDaySpecifiedResult, GetCurrencyRateDaySpecified> {

    private final CurrencyServiceClient currencyServiceClient;
    private final DateFormatService dateFormatService;

    public GetCurrencyRateDaySpecifiedHandler(CurrencyServiceClient currencyServiceClient, @Qualifier("cbrDateFormatService") DateFormatService dateFormatService) {
        this.currencyServiceClient = currencyServiceClient;
        this.dateFormatService = dateFormatService;
    }

    @Override
    public GetCurrencyRateDaySpecifiedResult handle(GetCurrencyRateDaySpecified query) {
        String date = dateFormatService.formatDate(query.getDate());
        Set<CurrencyRateDto> currencyRates = currencyServiceClient
                .getCurrencyRateDaySpecified(date).getCurrencyRates();
        return new GetCurrencyRateDaySpecifiedResult(currencyRates);
    }

}
