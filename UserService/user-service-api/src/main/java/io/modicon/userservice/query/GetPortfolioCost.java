package io.modicon.userservice.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetPortfolioCost implements Query<GetPortfolioCostResult> {

    private String id;

}
