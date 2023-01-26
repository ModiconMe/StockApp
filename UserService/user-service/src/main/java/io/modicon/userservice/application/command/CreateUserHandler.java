package io.modicon.userservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.userservice.application.UserMapper;
import io.modicon.userservice.command.CreateUser;
import io.modicon.userservice.command.CreateUserResult;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class CreateUserHandler implements CommandHandler<CreateUserResult, CreateUser> {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public CreateUserResult handle(CreateUser cmd) {
        if (userRepository.findById(cmd.getId()).isPresent())
            throw exception(HttpStatus.BAD_REQUEST, "user with id [%s] is already exist", cmd.getId());

        UserEntity user = UserEntity.builder()
                .id(cmd.getId())
                .name(cmd.getName())
                .build();
        userRepository.save(user);

        return new CreateUserResult(UserMapper.mapToDto(user));
    }

}
