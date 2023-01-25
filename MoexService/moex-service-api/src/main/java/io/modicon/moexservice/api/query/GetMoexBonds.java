package io.modicon.moexservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMoexBonds implements Query<GetMoexBondsResult> {

    private List<String> tickers;

}
