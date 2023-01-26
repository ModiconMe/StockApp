package io.modicon.userservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.userservice.operation.PortfolioStatisticOperation;
import io.modicon.userservice.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/statistic")
public class StatisticController implements PortfolioStatisticOperation {

    private final Bus bus;

    @Override
    public GetClassPercentStatResult getClassPercentStat(String userId) {
        return bus.executeQuery(new GetClassPercentStat(userId));
    }

    @Override
    public GetPortfolioCostResult getCostPortfolioStat(String userId) {
        return bus.executeQuery(new GetPortfolioCost(userId));
    }

    @Override
    public GetCostByTypeResult getCostByTypeStat(String userId, String type) {
        return bus.executeQuery(new GetCostByType(userId, type));
    }
}
