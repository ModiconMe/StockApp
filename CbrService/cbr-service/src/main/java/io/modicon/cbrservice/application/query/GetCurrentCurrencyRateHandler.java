package io.modicon.cbrservice.application.query;

import io.modicon.cbrservice.api.query.GetCurrentCurrencyRate;
import io.modicon.cbrservice.api.query.GetCurrentCurrencyRateResult;
import io.modicon.cbrservice.application.service.CbrCurrencyService;
import io.modicon.cqrsbus.QueryHandler;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCurrentCurrencyRateHandler implements QueryHandler<GetCurrentCurrencyRateResult, GetCurrentCurrencyRate> {

    private final CbrCurrencyService cbrCurrencyService;

    @Override
    public GetCurrentCurrencyRateResult handle(GetCurrentCurrencyRate query) {
        return new GetCurrentCurrencyRateResult(cbrCurrencyService.getCurrentCurrencyRates().stream()
                .map(cr -> new CurrencyRateDto(cr.name(), cr.ticker(), cr.rate())).collect(Collectors.toSet()));
    }

}
