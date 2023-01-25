package io.modicon.stockservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetStocksWithPrices implements Query<GetStocksWithPricesResult> {

    private List<String> figis;

}
