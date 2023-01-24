package io.modicon.moexservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.dto.BondPriceDto;
import io.modicon.moexservice.api.query.GetBondPrices;
import io.modicon.moexservice.api.query.GetBondPricesResult;
import io.modicon.moexservice.application.service.BondService;
import io.modicon.moexservice.domain.model.Bond;
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
public class GetBondPricesHandler implements QueryHandler<GetBondPricesResult, GetBondPrices> {

    private final BondService bondService;

    @Override
    public GetBondPricesResult handle(GetBondPrices query) {
        List<String> figis = new ArrayList<>(query.getFigis());
        log.info("request for figis {}", figis);

        List<Bond> corporateBonds = bondService.getCorporateBonds();
        List<Bond> govBonds = bondService.getGovBonds();

        List<Bond> bonds = new ArrayList<>(corporateBonds);
        bonds.addAll(govBonds);

        figis.removeAll(bonds.stream().map(Bond::ticker).toList());
        if (!figis.isEmpty())
            throw exception(HttpStatus.NOT_FOUND, "bonds %s not found", figis);

        return new GetBondPricesResult(
                bonds.stream()
                .filter(b -> query.getFigis().contains(b.ticker()))
                .map(b -> new BondPriceDto(b.ticker(), b.price().multiply(BigDecimal.valueOf(10)).doubleValue()))
                .toList());
    }

}
