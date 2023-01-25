package io.modicon.tinkoffservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetTinkoffStock implements Query<GetTinkoffStockResult> {

    private String figi;

}
