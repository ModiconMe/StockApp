package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.service.PortfolioCostService;
import io.modicon.userservice.application.service.StockWithPriceService;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.StockWithPrice;
import io.modicon.userservice.domain.model.TypeEntity;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.TypeDto;
import io.modicon.userservice.query.GetClassPercentStat;
import io.modicon.userservice.query.GetClassPercentStatResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class GetClassPercentStatHandler implements QueryHandler<GetClassPercentStatResult, GetClassPercentStat> {

    private final UserRepository userRepository;
    private final PortfolioCostService portfolioCostService;
    private final StockWithPriceService stockWithPriceService;

    @Transactional(readOnly = true)
    @Override
    public GetClassPercentStatResult handle(GetClassPercentStat query) {
        UserEntity user = userRepository.findById(query.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", query.getId()));

        Set<PositionEntity> positionsSet = user.getStocks();
        List<String> userFigis = positionsSet.stream().map(PositionEntity::getFigi).toList();

        List<StockWithPrice> stocks = stockWithPriceService.getStocksWithPrice(userFigis);
        Map<String, BigDecimal> positionsCostInRub = portfolioCostService.getPositionsCostInRub(stocks);

        Map<String, StockWithPrice> figiStockMap = stocks.stream().collect(Collectors.toMap(StockWithPrice::figi, s -> s));

        Map<StockWithPrice, BigDecimal> stockWithPricesCostMap = figiStockMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> positionsCostInRub.get(entry.getKey())));

        // get [quantity - cost] map
        Map<String, Integer> positionsMap = positionsSet.stream().collect(Collectors.toMap(PositionEntity::getFigi, PositionEntity::getQuantity));
        BigDecimal totalCost = new BigDecimal(0);
        for (Map.Entry<String, BigDecimal> entry : positionsCostInRub.entrySet()) {
            String key = entry.getKey();
            BigDecimal value = entry.getValue();
            Integer quantity = positionsMap.get(key);
            totalCost = totalCost.add(value.multiply(BigDecimal.valueOf(quantity)));
        }

        Map<TypeEntity, BigDecimal> typeCostMap = new HashMap<>();
        for (Map.Entry<StockWithPrice, BigDecimal> entry : stockWithPricesCostMap.entrySet()) {
            StockWithPrice key = entry.getKey();
            BigDecimal value = entry.getValue();
            Integer quantity = positionsMap.get(key.figi());
            if (!typeCostMap.containsKey(key.type()))
                typeCostMap.put(key.type(), value.multiply(BigDecimal.valueOf(quantity)));
            else
                typeCostMap.put(key.type(), typeCostMap.get(key.type()).add(value.multiply(BigDecimal.valueOf(quantity))));
        }

        final BigDecimal totalCostRes = totalCost;
        Map<TypeDto, BigDecimal> result = typeCostMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> TypeDto.valueOf(entry.getKey().getValue().toUpperCase()), entry -> entry.getValue().multiply(BigDecimal.valueOf(100)).divide(totalCostRes, 1, RoundingMode.HALF_UP)));

        return new GetClassPercentStatResult(user.getId(), result);
    }
}
