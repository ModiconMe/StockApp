package io.modicon.moexservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetBonds;
import io.modicon.moexservice.api.query.GetBondsResult;
import io.modicon.moexservice.application.BondMapper;
import io.modicon.moexservice.application.service.BondService;
import io.modicon.moexservice.domain.model.Bond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetBondsHandler implements QueryHandler<GetBondsResult, GetBonds> {

    private final BondService bondService;

    @Override
    public GetBondsResult handle(GetBonds query) {
        log.info("get bonds");
        List<String> tickers = query.getTickers();

        List<Bond> corporateBonds = bondService.getCorporateBonds();
        List<Bond> govBonds = bondService.getGovBonds();

        List<Bond> bonds = new ArrayList<>(corporateBonds);
        bonds.addAll(govBonds);

        return new GetBondsResult(
                bonds.stream()
                .filter(b -> tickers.contains(b.ticker()))
                .map(BondMapper::mapToDto)
                .toList());
    }

}
