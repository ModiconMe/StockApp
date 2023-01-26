package io.modicon.userservice.operation;

import io.modicon.userservice.query.GetClassPercentStatResult;
import io.modicon.userservice.query.GetCostByTypeResult;
import io.modicon.userservice.query.GetPortfolioCostResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PortfolioStatisticOperation {

    @GetMapping("/classes/{userId}")
    GetClassPercentStatResult getClassPercentStat(@PathVariable("userId") String userId);

    @GetMapping("/cost/{userId}")
    GetPortfolioCostResult getCostPortfolioStat(@PathVariable("userId") String userId);

    @GetMapping("/classes/{userId}/{type}")
    GetCostByTypeResult getCostByTypeStat(@PathVariable String userId, @PathVariable String type);

}
