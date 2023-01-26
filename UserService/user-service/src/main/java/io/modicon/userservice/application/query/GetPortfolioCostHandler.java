package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.service.PortfolioCostService;
import io.modicon.userservice.application.service.StockWithPriceService;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.StockWithPrice;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.query.GetPortfolioCost;
import io.modicon.userservice.query.GetPortfolioCostResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class GetPortfolioCostHandler implements QueryHandler<GetPortfolioCostResult, GetPortfolioCost> {

    private final UserRepository userRepository;
    private final PortfolioCostService portfolioCostService;
    private final StockWithPriceService stockWithPriceService;

    @Transactional(readOnly = true)
    @Override
    public GetPortfolioCostResult handle(GetPortfolioCost query) {
        UserEntity user = userRepository.findById(query.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", query.getId()));

        Set<PositionEntity> positionsSet = user.getStocks();
        List<String> userFigis = positionsSet.stream().map(PositionEntity::getFigi).toList();

        // get [figi, cost] map
        List<StockWithPrice> stocksWithPrice = stockWithPriceService.getStocksWithPrice(userFigis);
        Map<String, BigDecimal> positionsCostInRub = portfolioCostService.getPositionsCostInRub(stocksWithPrice);

        // get [figi - quantity] map
        Map<String, Integer> positionsMap = positionsSet.stream().collect(Collectors.toMap(PositionEntity::getFigi, PositionEntity::getQuantity));

//        // get [quantity - cost] map
//        Map<Integer, BigDecimal> quantityCost = positionsCostInRub.entrySet().stream()
//                .collect(Collectors.toMap(entry -> positionsMap.get(entry.getKey()), Map.Entry::getValue));

        BigDecimal totalCost = new BigDecimal(0);
        for (Map.Entry<String, BigDecimal> entry : positionsCostInRub.entrySet()) {
            String key = entry.getKey();
            BigDecimal value = entry.getValue();
            Integer quantity = positionsMap.get(key);
            totalCost = totalCost.add(value.multiply(BigDecimal.valueOf(quantity)));
        }

        return new GetPortfolioCostResult(totalCost);
    }

}
