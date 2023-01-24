package io.modicon.moexservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBondPricesResult;
import io.modicon.moexservice.application.service.BondService;
import io.modicon.moexservice.domain.model.Bond;
import io.modicon.stockservice.api.dto.StockPriceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.modicon.moexservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMoexBondPricesHandler implements QueryHandler<GetMoexBondPricesResult, GetMoexBondPrices> {

    private final BondService bondService;

    @Override
    public GetMoexBondPricesResult handle(GetMoexBondPrices query) {
        List<String> figis = new ArrayList<>(query.getFigis());
        log.info("request for figis {}", figis);

        List<Bond> corporateBonds = bondService.getCorporateBonds();
        List<Bond> govBonds = bondService.getGovBonds();

        List<Bond> bonds = new ArrayList<>(corporateBonds);
        bonds.addAll(govBonds);

        figis.removeAll(bonds.stream().map(Bond::ticker).toList());
        if (!figis.isEmpty())
            throw exception(HttpStatus.NOT_FOUND, "bonds %s not found", figis);

        return new GetMoexBondPricesResult(
                bonds.stream()
                .filter(b -> query.getFigis().contains(b.ticker()))
                .map(b -> new StockPriceDto(b.ticker(), b.price().multiply(BigDecimal.valueOf(10))))
                .toList());
    }

}
