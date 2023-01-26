package io.modicon.userservice.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetCostByType implements Query<GetCostByTypeResult> {

    private String id;
    private String type;

}
