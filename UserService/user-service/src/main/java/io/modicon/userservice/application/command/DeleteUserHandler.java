package io.modicon.userservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.userservice.command.DeleteUser;
import io.modicon.userservice.command.DeleteUserResult;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class DeleteUserHandler implements CommandHandler<DeleteUserResult, DeleteUser> {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public DeleteUserResult handle(DeleteUser cmd) {
        UserEntity user = userRepository.findById(cmd.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", cmd.getId()));

        userRepository.delete(user);

        return new DeleteUserResult();
    }

}
