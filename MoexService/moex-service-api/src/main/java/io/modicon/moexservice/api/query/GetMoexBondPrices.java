package io.modicon.moexservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMoexBondPrices implements Query<GetMoexBondPricesResult> {

    private List<String> figis;

}
