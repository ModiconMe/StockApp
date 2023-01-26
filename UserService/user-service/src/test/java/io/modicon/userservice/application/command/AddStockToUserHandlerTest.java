package io.modicon.userservice.application.command;

import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.userservice.application.client.StockServiceClient;
import io.modicon.userservice.command.AddStockToUser;
import io.modicon.userservice.command.AddStockToUserResult;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.StockRepository;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.PositionDto;
import io.modicon.userservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddStockToUserHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockServiceClient stockServiceClient;

    private AddStockToUserHandler addStockToUserHandler;

    @BeforeEach
    void setUp() {
        addStockToUserHandler = new AddStockToUserHandler(userRepository, stockRepository, stockServiceClient);
    }

    private Set<PositionEntity> userPositions = new HashSet<>();
    private UserEntity user = new UserEntity("1", "name", userPositions);
    private PositionEntity userPosition1 = new PositionEntity("figi1", 1);
    private PositionEntity userPosition2 = new PositionEntity("figi2", 2);
    private PositionEntity userPosition3 = new PositionEntity("figi3", 3);

    private Set<PositionDto> validPositionsToAdd = new HashSet<>();
    private Set<PositionDto> invalidPositionsToAdd = new HashSet<>();
    private PositionDto positionToAdd1 = new PositionDto("figi4", 4);
    private PositionDto positionToAdd2 = new PositionDto("figi5", 5);
    private PositionDto positionToAdd3 = new PositionDto("figi6", 6);
    private PositionDto positionToAdd4 = new PositionDto("figi7", 7);

    private List<StockDto> stockDtos = new ArrayList<>();
    private StockDto stockDto1 = new StockDto("figi4", "figi4", "name", "share", CurrencyDto.EUR, "TINKOFF");
    private StockDto stockDto2 = new StockDto("figi5", "figi5", "name", "share", CurrencyDto.EUR, "TINKOFF");
    private StockDto stockDto3 = new StockDto("figi6", "figi6", "name", "share", CurrencyDto.EUR, "TINKOFF");
    private StockDto stockDto4 = new StockDto("figi7", "figi7", "name", "share", CurrencyDto.EUR, "TINKOFF");

    private PositionDto userPositionDto1 = new PositionDto("figi1", 1);
    private PositionDto userPositionDto2 = new PositionDto("figi2", 2);
    private PositionDto userPositionDto3 = new PositionDto("figi3", 3);
    private Set<PositionDto> resultSet1 = new HashSet<>();
    private Set<PositionDto> resultSet2 = new HashSet<>();
    {
        userPositions.add(userPosition1);
        userPositions.add(userPosition2);
        userPositions.add(userPosition3);

        validPositionsToAdd.add(positionToAdd1);
        validPositionsToAdd.add(positionToAdd2);
        validPositionsToAdd.add(positionToAdd3);
        validPositionsToAdd.add(positionToAdd4);

        invalidPositionsToAdd.add(positionToAdd1);
        invalidPositionsToAdd.add(userPositionDto3);

        stockDtos.add(stockDto1);
        stockDtos.add(stockDto2);
        stockDtos.add(stockDto3);
        stockDtos.add(stockDto4);

        resultSet1.add(userPositionDto1);
        resultSet1.add(userPositionDto2);
        resultSet1.add(userPositionDto3);
        resultSet1.add(positionToAdd1);
        resultSet1.add(positionToAdd2);
        resultSet1.add(positionToAdd3);
        resultSet1.add(positionToAdd4);

        resultSet2.add(userPositionDto1);
        resultSet2.add(userPositionDto2);
        resultSet2.add(userPositionDto3);
        resultSet2.add(positionToAdd2);
        resultSet2.add(positionToAdd3);
        resultSet2.add(positionToAdd4);
    }

    @Test
    void should_returnCorrectData() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(stockRepository.existsByFigi(anyString())).thenReturn(false);
        when(stockServiceClient.getStocks(any()))
                .thenReturn(new GetStocksResult(stockDtos, new HashSet<>()));

        AddStockToUserResult result = addStockToUserHandler.handle(new AddStockToUser(user.getId(), validPositionsToAdd));

        assertThat(result.getNotFoundFigis()).isEmpty();
        assertThat(result.getUser().stocks()).isNotEmpty();
        assertThat(result.getUser().stocks().size()).isEqualTo(resultSet1.size());
        assertThat(result.getUser().stocks()).isEqualTo(resultSet1);
    }

    @Test
    void should_returnCorrectData_whenAllExistInDb() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(stockRepository.existsByFigi(anyString())).thenReturn(true);

        AddStockToUserResult result = addStockToUserHandler.handle(new AddStockToUser(user.getId(), validPositionsToAdd));

        assertThat(result.getNotFoundFigis()).isEmpty();
        assertThat(result.getUser().stocks()).isNotEmpty();
        assertThat(result.getUser().stocks().size()).isEqualTo(resultSet1.size());
        assertThat(result.getUser().stocks()).isEqualTo(resultSet1);
    }

    @Test
    void should_returnCorrectData_whenStocksNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(stockRepository.existsByFigi(anyString())).thenReturn(false);
        HashSet<String> notFoundFigis = new HashSet<>();
        String notFoundFigi = "figi4";
        notFoundFigis.add(notFoundFigi);
        when(stockServiceClient.getStocks(any()))
                .thenReturn(new GetStocksResult(stockDtos, notFoundFigis));

        AddStockToUserResult result = addStockToUserHandler.handle(new AddStockToUser(user.getId(), validPositionsToAdd));

        assertThat(result.getNotFoundFigis()).isNotEmpty();
        assertThat(result.getNotFoundFigis().contains(notFoundFigi)).isTrue();

        assertThat(result.getUser().stocks()).isNotEmpty();
        assertThat(result.getUser().stocks().size()).isEqualTo(resultSet2.size());
        assertThat(result.getUser().stocks()).isEqualTo(resultSet2);
    }

    @Test
    void should_throw_whenPositionAlreadyExist() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> addStockToUserHandler.handle(new AddStockToUser(user.getId(), invalidPositionsToAdd)))
                .isInstanceOf(ApiException.class);
    }

}