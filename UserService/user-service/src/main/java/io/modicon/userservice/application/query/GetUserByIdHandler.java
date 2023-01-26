package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.UserMapper;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.query.GetUserById;
import io.modicon.userservice.query.GetUserByIdResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class GetUserByIdHandler implements QueryHandler<GetUserByIdResult, GetUserById> {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public GetUserByIdResult handle(GetUserById query) {
        return new GetUserByIdResult(UserMapper.mapToDto(userRepository.findById(query.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", query.getId()))));
    }
}
