package io.modicon.userservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.userservice.application.UserMapper;
import io.modicon.userservice.command.ChangeUsersStock;
import io.modicon.userservice.command.ChangeUsersStockResult;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.UserRepository;
import io.modicon.userservice.dto.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class ChangeUsersStocksHandler implements CommandHandler<ChangeUsersStockResult, ChangeUsersStock> {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChangeUsersStockResult handle(ChangeUsersStock cmd) {
        UserEntity user = userRepository.findById(cmd.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", cmd.getId()));

        PositionDto positionDto = cmd.getPosition();

        // check that position exist
        PositionEntity positionEntity = user.getStocks().stream()
                .filter(position -> position.getFigi().equals(positionDto.figi()))
                .findAny()
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "position with figi [%s] is not exist", positionDto.figi()));

        Integer quantity = positionDto.quantity();
        if (quantity <= 0)
            user.getStocks().remove(positionEntity);
        else
            positionEntity.setQuantity(quantity);
        userRepository.save(user);

        return new ChangeUsersStockResult(UserMapper.mapToDto(user));
    }

}
