package io.modicon.cbrservice.api.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCurrencyRateDaySpecified implements Query<GetCurrencyRateDaySpecifiedResult> {

    private String date;

}
