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
import io.modicon.userservice.infrastructure.exception.ApiException;
import io.modicon.userservice.query.GetCostByType;
import io.modicon.userservice.query.GetCostByTypeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class GetCostByTypeHandler implements QueryHandler<GetCostByTypeResult, GetCostByType> {

    private final UserRepository userRepository;
    private final PortfolioCostService portfolioCostService;
    private final StockWithPriceService stockWithPriceService;

    @Transactional(readOnly = true)
    @Override
    public GetCostByTypeResult handle(GetCostByType query) {
        TypeEntity requestedType = null;
        try {
            requestedType = TypeEntity.valueOf(query.getType().toUpperCase());
        } catch (Exception e) {
            throw ApiException.exception(HttpStatus.NOT_FOUND, "invalid stock type, if must be in: %s", Arrays.toString(TypeEntity.values()));
        }

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

        BigDecimal result = BigDecimal.valueOf(0);

        for (Map.Entry<StockWithPrice, BigDecimal> entry : stockWithPricesCostMap.entrySet()) {
            if (entry.getKey().type().equals(requestedType)) {
                Integer quantity = positionsMap.get(entry.getKey().figi());
                result = result.add(entry.getValue().multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return new GetCostByTypeResult(TypeDto.valueOf(requestedType.getValue().toUpperCase()), result);
    }
}
