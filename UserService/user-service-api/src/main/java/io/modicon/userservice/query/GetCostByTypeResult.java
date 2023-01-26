package io.modicon.userservice.query;

import io.modicon.userservice.dto.TypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetCostByTypeResult {

    private TypeDto type;
    private BigDecimal cost;

}
