package io.modicon.moexservice.api.operation;

import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.moexservice.api.query.GetMoexBondsResult;
import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBondPricesResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MoexServiceOperation {

    @PostMapping
    GetMoexBondsResult getBonds(@RequestBody GetMoexBonds query);

    @PostMapping("/prices")
    GetMoexBondPricesResult getPricesByFigis(@RequestBody GetMoexBondPrices query);
}
