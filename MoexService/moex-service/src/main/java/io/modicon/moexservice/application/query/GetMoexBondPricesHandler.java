package io.modicon.moexservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBondPricesResult;
import io.modicon.moexservice.application.service.MoexBondService;
import io.modicon.moexservice.domain.model.Bond;
import io.modicon.stockservice.api.dto.StockPriceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMoexBondPricesHandler implements QueryHandler<GetMoexBondPricesResult, GetMoexBondPrices> {

    private final MoexBondService bondService;

    @Override
    public GetMoexBondPricesResult handle(GetMoexBondPrices query) {
        List<String> figis = new ArrayList<>(query.getFigis());
        log.info("request for figis {}", figis);

        List<Bond> corporateBonds = bondService.getCorporateBonds();
        List<Bond> govBonds = bondService.getGovBonds();

        List<Bond> bonds = new ArrayList<>(corporateBonds);
        bonds.addAll(govBonds);

        return new GetMoexBondPricesResult(bonds.stream()
                .filter(b -> query.getFigis().contains(b.ticker()))
                .map(b -> new StockPriceDto(b.ticker(), b.price().multiply(BigDecimal.valueOf(10))))
                .toList());
    }

}
