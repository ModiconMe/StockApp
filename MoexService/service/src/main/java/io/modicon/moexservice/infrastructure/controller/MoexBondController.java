package io.modicon.moexservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.moexservice.api.operation.MoexBondOperation;
import io.modicon.moexservice.api.query.GetBonds;
import io.modicon.moexservice.api.query.GetBondsResult;
import io.modicon.moexservice.api.query.GetBondPrices;
import io.modicon.moexservice.api.query.GetBondPricesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bonds/")
public class MoexBondController implements MoexBondOperation {

    private final Bus bus;

    @Override
    public GetBondsResult getBonds(GetBonds query) {
        return bus.executeQuery(query);
    }

    @Override
    public GetBondPricesResult getPricesByFigis(GetBondPrices query) {
        return bus.executeQuery(query);
    }
}
