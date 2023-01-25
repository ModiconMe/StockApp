package io.modicon.moexservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.moexservice.api.operation.MoexServiceOperation;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.moexservice.api.query.GetMoexBondsResult;
import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBondPricesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bonds")
public class MoexBondController implements MoexServiceOperation {

    private final Bus bus;

    @Override
    public GetMoexBondsResult getBonds(GetMoexBonds query) {
        return bus.executeQuery(query);
    }

    @Override
    public GetMoexBondPricesResult getPricesByFigis(GetMoexBondPrices query) {
        return bus.executeQuery(query);
    }
}
