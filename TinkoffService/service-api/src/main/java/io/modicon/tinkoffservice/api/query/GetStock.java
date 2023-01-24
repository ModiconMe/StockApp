package io.modicon.tinkoffservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetStock implements Query<GetStockResult> {

    private String figi;

}
