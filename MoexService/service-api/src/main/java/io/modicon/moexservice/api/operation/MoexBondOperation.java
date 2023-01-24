package io.modicon.moexservice.api.operation;

import io.modicon.moexservice.api.query.GetBonds;
import io.modicon.moexservice.api.query.GetBondsResult;
import io.modicon.moexservice.api.query.GetBondPrices;
import io.modicon.moexservice.api.query.GetBondPricesResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MoexBondOperation {

    @PostMapping
    GetBondsResult getBonds(@RequestBody GetBonds query);

    @PostMapping("/prices")
    GetBondPricesResult getPricesByFigis(@RequestBody GetBondPrices query);
}
