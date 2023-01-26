package io.modicon.tinkoffservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetTinkoffStocks implements Query<GetTinkoffStocksResult> {

    private List<String> figis;

}
