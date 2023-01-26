package io.modicon.userservice.application.command;

import io.modicon.userservice.command.CreateUser;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.UserDto;
import io.modicon.userservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserHandlerTest {

    @Mock
    private UserRepository userRepository;

    private CreateUserHandler createUserHandler;

    private UserEntity user = new UserEntity("1", "name", new HashSet<>());

    @BeforeEach
    void setUp() {
        createUserHandler = new CreateUserHandler(userRepository);
    }

    @Test
    void should_returnCorrectData_whenUserNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserDto result = createUserHandler.handle(new CreateUser(user.getId(), user.getName())).getUser();

        assertThat(result.id()).isEqualTo(user.getId());
        assertThat(result.name()).isEqualTo(user.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_throw_whenUserAlreadyExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ApiException apiException = catchThrowableOfType(() -> createUserHandler.handle(new CreateUser(user.getId(), user.getName())), ApiException.class);
        assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userRepository, times(0)).save(user);
    }
}