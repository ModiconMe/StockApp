package io.modicon.cbrservice.application.query;

import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecified;
import io.modicon.cbrservice.api.query.GetCurrencyRateDaySpecifiedResult;
import io.modicon.cbrservice.application.service.CbrCurrencyService;
import io.modicon.cbrservice.application.service.DateFormatService;
import io.modicon.cqrsbus.QueryHandler;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetCurrencyRateDaySpecifiedHandler implements QueryHandler<GetCurrencyRateDaySpecifiedResult, GetCurrencyRateDaySpecified> {

    private final CbrCurrencyService cbrCurrencyService;
    private final DateFormatService dateParserService;

    @Override
    public GetCurrencyRateDaySpecifiedResult handle(GetCurrencyRateDaySpecified query) {
        String date = dateParserService.formatDate(query.getDate());

        return new GetCurrencyRateDaySpecifiedResult(cbrCurrencyService.getCurrencyRatesDaySpecified(date).stream()
                .map(cr -> new CurrencyRateDto(cr.name(), cr.ticker(), cr.rate())).collect(Collectors.toSet()));
    }

}
