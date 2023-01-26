package io.modicon.userservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.userservice.application.UserMapper;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.query.GetUsers;
import io.modicon.userservice.query.GetUsersResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAllUsersHandler implements QueryHandler<GetUsersResult, GetUsers> {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public GetUsersResult handle(GetUsers query) {
        return new GetUsersResult(userRepository.findAll().stream().map(UserMapper::mapToDto).toList());
    }
}
