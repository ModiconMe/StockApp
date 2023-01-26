package io.modicon.userservice.query;

import io.modicon.userservice.dto.TypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetClassPercentStatResult {

    private String id;
    private Map<TypeDto, BigDecimal> typePercentPortfolioMap;

}
