package io.modicon.moexservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.moexservice.api.query.GetMoexBondsResult;
import io.modicon.moexservice.application.BondMapper;
import io.modicon.moexservice.application.service.MoexBondService;
import io.modicon.moexservice.domain.model.Bond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMoexBondsHandler implements QueryHandler<GetMoexBondsResult, GetMoexBonds> {

    private final MoexBondService bondService;

    @Override
    public GetMoexBondsResult handle(GetMoexBonds query) {
        List<String> tickers = query.getTickers();
        log.info("get bonds {}", tickers);

        List<Bond> corporateBonds = bondService.getCorporateBonds();
        List<Bond> govBonds = bondService.getGovBonds();

        List<Bond> bonds = new ArrayList<>(corporateBonds);
        bonds.addAll(govBonds);

        return new GetMoexBondsResult(bonds.stream()
                .filter(b -> tickers.contains(b.ticker()))
                .map(BondMapper::mapToDto)
                .toList());
    }

}
