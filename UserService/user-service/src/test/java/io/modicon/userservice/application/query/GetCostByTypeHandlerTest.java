package io.modicon.userservice.application.query;

import io.modicon.userservice.application.service.PortfolioCostService;
import io.modicon.userservice.application.service.StockWithPriceService;
import io.modicon.userservice.domain.model.Currency;
import io.modicon.userservice.domain.model.*;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.TypeDto;
import io.modicon.userservice.infrastructure.exception.ApiException;
import io.modicon.userservice.query.GetCostByType;
import io.modicon.userservice.query.GetCostByTypeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCostByTypeHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PortfolioCostService portfolioCostService;
    @Mock
    private StockWithPriceService stockWithPriceService;

    private GetCostByTypeHandler getPortfolioCostHandler;

    @BeforeEach
    void setUp() {
        getPortfolioCostHandler = new GetCostByTypeHandler(userRepository, portfolioCostService, stockWithPriceService);
    }

    private BigDecimal rubCost = BigDecimal.valueOf(1);
    private BigDecimal usdCost = BigDecimal.valueOf(65);
    private BigDecimal eurCost = BigDecimal.valueOf(75);
    private BigDecimal gbpCost = BigDecimal.valueOf(86);
    private BigDecimal cnyCost = BigDecimal.valueOf(10);
    private BigDecimal chfCost = BigDecimal.valueOf(76);
    private BigDecimal hkdCost = BigDecimal.valueOf(99);
    private BigDecimal jpyCost = BigDecimal.valueOf(53);

    private Set<PositionEntity> userPositions = new HashSet<>();
    private UserEntity user = new UserEntity("1", "name", userPositions);
    private PositionEntity userPosition1 = new PositionEntity("figi1", 1);
    private PositionEntity userPosition2 = new PositionEntity("figi2", 2);
    private PositionEntity userPosition3 = new PositionEntity("figi3", 3);
    private PositionEntity userPosition4 = new PositionEntity("figi4", 4);

    private List<StockWithPrice> stocks = new ArrayList<>();
    private StockWithPrice stock1 = new StockWithPrice("figi1", "figi1", "name1", TypeEntity.SHARE, Currency.EUR, "TINKOFF", BigDecimal.valueOf(10));
    private StockWithPrice stock2 = new StockWithPrice("figi2", "figi2", "name2", TypeEntity.SHARE, Currency.GBP, "TINKOFF", BigDecimal.valueOf(50));
    private StockWithPrice stock3 = new StockWithPrice("figi3", "figi3", "name3", TypeEntity.ETF, Currency.RUB, "TINKOFF", BigDecimal.valueOf(2000));
    private StockWithPrice stock4 = new StockWithPrice("figi4", "figi4", "name4", TypeEntity.BOND, Currency.USD, "MOEX", BigDecimal.valueOf(10));

    private List<String> userFigis = new ArrayList<>();

    private Map<String, BigDecimal> positionsCostInRub = Map.of(
            "figi1", stock1.price().multiply(eurCost),
            "figi2", stock2.price().multiply(gbpCost),
            "figi3", stock3.price().multiply(rubCost),
            "figi4", stock4.price().multiply(usdCost)
    );

    private BigDecimal shareCost = stock1.price().multiply(eurCost).multiply(BigDecimal.valueOf(userPosition1.getQuantity()))
            .add(stock2.price().multiply(gbpCost).multiply(BigDecimal.valueOf(userPosition2.getQuantity())));
    private BigDecimal etfCost = stock3.price().multiply(rubCost).multiply(BigDecimal.valueOf(userPosition3.getQuantity()));
    private BigDecimal bondCost = stock4.price().multiply(usdCost).multiply(BigDecimal.valueOf(userPosition4.getQuantity()));
    private BigDecimal totalCost = shareCost.add(etfCost).add(bondCost);


    {
        userPositions.add(userPosition1);
        userPositions.add(userPosition2);
        userPositions.add(userPosition3);
        userPositions.add(userPosition4);

        stocks.add(stock1);
        stocks.add(stock2);
        stocks.add(stock3);
        stocks.add(stock4);

        userFigis.add(stock1.figi());
        userFigis.add(stock2.figi());
        userFigis.add(stock3.figi());
        userFigis.add(stock4.figi());
    }

    @Test
    void should_returnCostOfETFTypeInUsersPortfolio() {
        when(stockWithPriceService.getStocksWithPrice(any())).thenReturn(stocks);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(portfolioCostService.getPositionsCostInRub(any())).thenReturn(positionsCostInRub);

        TypeEntity type = TypeEntity.ETF;
        GetCostByTypeResult expected = getPortfolioCostHandler.handle(new GetCostByType(user.getId(), type.getValue()));

        assertThat(expected.getType()).isEqualTo(TypeDto.valueOf(type.getValue().toUpperCase()));
        assertThat(expected.getCost()).isEqualTo(etfCost);
    }

    @Test
    void should_returnCostOfSHARETypeInUsersPortfolio() {
        when(stockWithPriceService.getStocksWithPrice(any())).thenReturn(stocks);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(portfolioCostService.getPositionsCostInRub(any())).thenReturn(positionsCostInRub);

        TypeEntity type = TypeEntity.SHARE;
        GetCostByTypeResult expected = getPortfolioCostHandler.handle(new GetCostByType(user.getId(), type.getValue()));

        assertThat(expected.getType()).isEqualTo(TypeDto.valueOf(type.getValue().toUpperCase()));
        assertThat(expected.getCost()).isEqualTo(shareCost);
    }

    @Test
    void should_returnCostOfBONDTypeInUsersPortfolio() {
        when(stockWithPriceService.getStocksWithPrice(any())).thenReturn(stocks);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(portfolioCostService.getPositionsCostInRub(any())).thenReturn(positionsCostInRub);

        TypeEntity type = TypeEntity.BOND;
        GetCostByTypeResult expected = getPortfolioCostHandler.handle(new GetCostByType(user.getId(), type.getValue()));

        assertThat(expected.getType()).isEqualTo(TypeDto.valueOf(type.getValue().toUpperCase()));
        assertThat(expected.getCost()).isEqualTo(bondCost);
    }

    @Test
    void should_throw_whenInvalidTypeProvided() {
        ApiException exception = catchThrowableOfType(() -> getPortfolioCostHandler
                .handle(new GetCostByType(user.getId(), "InvalidType")), ApiException.class);

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_throw_whenUserIsNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        TypeEntity type = TypeEntity.BOND;
        ApiException exception = catchThrowableOfType(() -> getPortfolioCostHandler
                .handle(new GetCostByType(user.getId(), type.getValue())), ApiException.class);

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}