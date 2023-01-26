package io.modicon.userservice.application.command;

import io.modicon.cqrsbus.CommandHandler;
import io.modicon.stockservice.api.query.GetStocks;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.userservice.application.PositionMapper;
import io.modicon.userservice.application.StockMapper;
import io.modicon.userservice.application.UserMapper;
import io.modicon.userservice.application.client.StockServiceClient;
import io.modicon.userservice.command.AddStockToUser;
import io.modicon.userservice.command.AddStockToUserResult;
import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.domain.model.StockEntity;
import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.domain.repository.StockRepository;
import io.modicon.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@RequiredArgsConstructor
@Service
public class AddStockToUserHandler implements CommandHandler<AddStockToUserResult, AddStockToUser> {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StockServiceClient stockServiceClient;

    @Transactional
    @Override
    public AddStockToUserResult handle(AddStockToUser cmd) {
        UserEntity user = userRepository.findById(cmd.getId())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with id [%s] is not exist", cmd.getId()));

        Set<PositionEntity> userPositions = user.getStocks();
        Set<PositionEntity> requestPositions = cmd.getPositions().stream()
                .map(PositionMapper::mapToEntity)
                .filter(position -> position.getQuantity() > 0).collect(Collectors.toSet());

        // check that user has this figis
        requestPositions.forEach(p -> {
            if (userPositions.contains(p))
                throw exception(HttpStatus.BAD_REQUEST, "you already has stock with that figi: %s", p.getFigi());
        });

        requestPositions.removeAll(userPositions); // remove if figi is equal

        // check stock in database
        requestPositions.forEach(position -> {
            if (stockRepository.existsByFigi(position.getFigi())) {
                userPositions.add(position);
            }
        });
        requestPositions.removeAll(userPositions);

        // go to StockService
        List<String> figis = requestPositions.stream().map(PositionEntity::getFigi).toList();
        Set<String> notFoundFigis = new HashSet<>();
        if (!requestPositions.isEmpty()) {
            GetStocksResult stockFromService = stockServiceClient.getStocks(new GetStocks(figis));
            List<StockEntity> stocks = stockFromService.getStocks()
                    .stream().map(StockMapper::mapToEntity).toList();
            stocks.forEach(stock -> stock.setId(UUID.randomUUID()));
            stockRepository.saveAll(stocks);

            notFoundFigis = stockFromService.getNotFoundFigis();

            Set<PositionEntity> positionNotFound = new HashSet<>();
            for (PositionEntity position : requestPositions) {
                if (notFoundFigis.contains(position.getFigi())) {
                    positionNotFound.add(position);
                }
            }
            requestPositions.removeAll(positionNotFound);
        }

        // add to stock to user
        userPositions.addAll(requestPositions);
        user.setStocks(userPositions);
        userRepository.save(user);

        return new AddStockToUserResult(UserMapper.mapToDto(user), notFoundFigis);
    }

}
