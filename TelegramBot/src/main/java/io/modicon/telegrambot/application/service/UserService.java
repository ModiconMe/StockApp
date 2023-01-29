package io.modicon.telegrambot.application.service;

import io.modicon.telegrambot.application.client.StatOperationsClient;
import io.modicon.telegrambot.application.client.UserOperationsClient;
import io.modicon.telegrambot.config.TelegramBotException;
import io.modicon.telegrambot.model.PortfolioStatUtils;
import io.modicon.userservice.command.*;
import io.modicon.userservice.dto.TypeDto;
import io.modicon.userservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserOperationsClient userOperationsClient;
    private final StatOperationsClient statOperationsClient;

    public UserDto registerUser(CreateUser user) {
        try {
            return userOperationsClient.getUserById(user.getId()).getUser();
        } catch (TelegramBotException e) {
            return userOperationsClient.createUser(user).getUser();
        }
    }

    public UserDto getUserById(String chatId) {
        return userOperationsClient.getUserById(chatId).getUser();
    }

    public AddStockToUserResult addStockToUser(String chatId, AddStockToUser cmd) {
        return userOperationsClient.addStockToUser(chatId, cmd);
    }

    public ChangeUsersStockResult updateUserStock(String chatId, ChangeUsersStock cmd) {
        return userOperationsClient.changeUsersStock(chatId, cmd);
    }

    public PortfolioStatUtils getPortfolioInformation(String chatId) {
        UserDto user = userOperationsClient.getUserById(chatId).getUser();
        BigDecimal portfolioCost = statOperationsClient.getCostPortfolioStat(chatId).getCost();
        Map<TypeDto, BigDecimal> typePercentPortfolioMap = statOperationsClient.getClassPercentStat(chatId).getTypePercentPortfolioMap();
        return new PortfolioStatUtils(user, portfolioCost, typePercentPortfolioMap);
    }
}
