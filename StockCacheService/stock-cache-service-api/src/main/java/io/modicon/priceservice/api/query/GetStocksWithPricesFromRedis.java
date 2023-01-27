package io.modicon.priceservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetStocksWithPricesFromRedis implements Query<GetStocksWithPricesFromRedisResult> {

    private List<String> figis;

}
