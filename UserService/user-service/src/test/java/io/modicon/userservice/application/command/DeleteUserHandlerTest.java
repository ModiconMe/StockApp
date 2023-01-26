package io.modicon.userservice.application.command;

import io.modicon.userservice.command.DeleteUser;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserHandlerTest {

    @Mock
    private UserRepository userRepository;

    private DeleteUserHandler deleteUserHandler;

    private UserEntity user = new UserEntity("1", "name", new HashSet<>());

    @BeforeEach
    void setUp() {
        deleteUserHandler = new DeleteUserHandler(userRepository);
    }

    @Test
    void should_deleteUser_whenUserExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        deleteUserHandler.handle(new DeleteUser(user.getId()));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void should_throw_whenUserNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ApiException apiException = catchThrowableOfType(() -> deleteUserHandler.handle(new DeleteUser(user.getId())), ApiException.class);
        assertThat(apiException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userRepository, times(0)).delete(user);
    }

}