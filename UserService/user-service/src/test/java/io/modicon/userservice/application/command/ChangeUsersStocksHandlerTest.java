package io.modicon.userservice.application.command;

import io.modicon.userservice.command.ChangeUsersStock;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.PositionDto;
import io.modicon.userservice.dto.UserDto;
import io.modicon.userservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChangeUsersStocksHandlerTest {

    @Mock
    private UserRepository userRepository;

    private ChangeUsersStocksHandler changeUsersStocksHandler;

    @BeforeEach
    void setUp() {
        changeUsersStocksHandler = new ChangeUsersStocksHandler(userRepository);
    }

    private Set<PositionEntity> userPositions = new HashSet<>();
    private UserEntity user = new UserEntity("1", "name", userPositions);
    private PositionEntity userPosition1 = new PositionEntity("figi1", 1, "name1");
    private PositionEntity userPosition2 = new PositionEntity("figi2", 2, "name2");
    private PositionEntity userPosition3 = new PositionEntity("figi3", 3, "name3");
    private PositionDto newUserPosition1 = new PositionDto("figi1", 25, "name1");
    private PositionDto notExistedUserPosition = new PositionDto("figi4", 25, "name4");

    private Set<PositionDto> oldUserPositions = new HashSet<>();
    private PositionDto oldUserPosition1 = new PositionDto("figi1", 1, "name1");
    private PositionDto oldUserPosition2 = new PositionDto("figi2", 2, "name2");
    private PositionDto oldUserPosition3 = new PositionDto("figi3", 3, "name3");

    private Set<PositionDto> newUserPositions = new HashSet<>();
    private PositionDto newPositionDto1 = new PositionDto("figi1", 25, "name1");
    private PositionDto newPositionDto2 = new PositionDto("figi2", 2, "name2");
    private PositionDto newPositionDto3 = new PositionDto("figi3", 3, "name3");

    private Set<PositionDto> newUserPositionsWhenDeletedZero = new HashSet<>();
    private PositionDto newUserPositionsWhenDeletedZero1 = new PositionDto("figi1", 1, "name1");
    private PositionDto newUserPositionsWhenDeletedZero2 = new PositionDto("figi3", 3, "name3");
    private PositionDto positionToDelete = new PositionDto("figi2", 0, "name2");

    {
        userPositions.add(userPosition1);
        userPositions.add(userPosition2);
        userPositions.add(userPosition3);

        newUserPositions.add(newPositionDto1);
        newUserPositions.add(newPositionDto2);
        newUserPositions.add(newPositionDto3);

        oldUserPositions.add(oldUserPosition1);
        oldUserPositions.add(oldUserPosition2);
        oldUserPositions.add(oldUserPosition3);

        newUserPositionsWhenDeletedZero.add(newUserPositionsWhenDeletedZero1);
        newUserPositionsWhenDeletedZero.add(newUserPositionsWhenDeletedZero2);
    }

    @Test
    void should_returnCorrectData() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        UserDto updatedUser = changeUsersStocksHandler.handle(new ChangeUsersStock(user.getId(), newUserPosition1)).getUser();

        assertThat(updatedUser.id()).isEqualTo(user.getId());
        assertThat(updatedUser.name()).isEqualTo(user.getName());
        assertThat(updatedUser.stocks()).isNotEmpty();
        assertThat(updatedUser.stocks().size()).isEqualTo(newUserPositions.size());
        assertThat(updatedUser.stocks()).isEqualTo(newUserPositions);
    }

    @Test
    void should_deleteWhenPosition_whenQuantityEqual0() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        UserDto updatedUser = changeUsersStocksHandler.handle(new ChangeUsersStock(user.getId(), positionToDelete)).getUser();

        assertThat(updatedUser.id()).isEqualTo(user.getId());
        assertThat(updatedUser.name()).isEqualTo(user.getName());
        assertThat(updatedUser.stocks()).isNotEmpty();
        assertThat(updatedUser.stocks().size()).isEqualTo(newUserPositionsWhenDeletedZero.size());
        assertThat(updatedUser.stocks()).isEqualTo(newUserPositionsWhenDeletedZero);
    }

    @Test
    void should_throw_whenPositionNotExist() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> changeUsersStocksHandler.handle(new ChangeUsersStock(user.getId(), notExistedUserPosition)).getUser())
                .isInstanceOf(ApiException.class);
    }



}