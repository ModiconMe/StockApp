package io.modicon.userservice.query;

import io.modicon.cqrsbus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetClassPercentStat implements Query<GetClassPercentStatResult> {

    private String id;

}
