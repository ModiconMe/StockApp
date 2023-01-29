package io.modicon.telegrambot.bot;

import com.vdurmont.emoji.EmojiParser;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import io.modicon.telegrambot.application.service.BotParamHandler;
import io.modicon.telegrambot.application.service.CurrencyService;
import io.modicon.telegrambot.application.service.StockSearchService;
import io.modicon.telegrambot.application.service.UserService;
import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.telegrambot.config.TelegramBotException;
import io.modicon.userservice.command.*;
import io.modicon.userservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public final class Bot extends TelegramLongPollingBot {

    private final ApplicationConfig botConfig;
    private final UserService userService;
    private final BotParamHandler botParamHandler;
    private final CurrencyService currencyService;
    private final StockSearchService stockSearchService;

    public Bot(ApplicationConfig botConfig, UserService userService, BotParamHandler botParamHandler, CurrencyService currencyService, StockSearchService stockSearchService) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.botParamHandler = botParamHandler;
        this.currencyService = currencyService;
        this.stockSearchService = stockSearchService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getApiKey();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            String chatId = String.valueOf(update.getMessage().getChatId());

            String command = message;
            String param = null;
            int paramBegin = message.indexOf(" ");
            if (paramBegin != -1) {
                command = message.substring(0, paramBegin);
                param = message.substring(paramBegin + 1);
            }

            BotCommandEnum botCommand = BotCommandEnum.getCommand(command);

            if (botCommand != null) {
                switch (botCommand) {
                    case START -> {
                        registerUser(chatId, update.getMessage());
                        startCommandReceiver(chatId);
                    }
                    case HELP -> {
                    }
                    case ADD_STOCK -> {
                        addStockCommandReceiver(chatId, param);
                    }
                    case DELETE_STOCK -> {
                        deleteUsersStockCommandReceiver(chatId, param);
                    }
                    case UPDATE_STOCK -> {
                        changeUsersStockCommandReceiver(chatId, param);
                    }
                    case GET_PORTFOLIO_INFO -> {
                        executeMessage(new SendMessage(chatId, userService.getPortfolioInformation(chatId).toString()));
                    }
                    case CURRENCIES_RATES -> {
                        getCurrentCurrencyRate(chatId, param);
                    }
                    case FIND_STOCK_BY_TICKER -> {
                        findStockByTicker(chatId, param);
                    }
                }
            } else {
                executeMessage(new SendMessage(chatId, "Извините, я вас не понимаю. Попробуйте /help"));
            }
        }
    }

    private void registerUser(String chatId, Message msg) {
        var username = msg.getChat().getUserName() == null ? msg.getChat().getFirstName() + msg.getChat().getLastName() : msg.getChat().getUserName();
        UserDto user = userService.registerUser(new CreateUser(chatId, username));
        log.info("register user {}", user);
    }

    private void startCommandReceiver(String chatId) {
        UserDto user = userService.getUserById(chatId);
        String username = user.name();
        String answer = EmojiParser.parseToUnicode(String.format("Hi, %s, nice to meet you! :blush:", username));
        executeMessage(new SendMessage(chatId, answer));
        log.info("Replied to user {}", username);
    }

    private void addStockCommandReceiver(String chatId, String param) {
        try {
            AddStockToUserResult addStockToUserResult = userService.addStockToUser(chatId, new AddStockToUser("", botParamHandler.parseAddStockParam(param)));
            if (!addStockToUserResult.getNotFoundFigis().isEmpty())
                executeMessage(new SendMessage(chatId, "Не удалось найти:" + addStockToUserResult.getNotFoundFigis()));
            executeMessage(new SendMessage(chatId, userService.getPortfolioInformation(chatId).toString()));
        } catch (Exception e) {
            executeMessage(new SendMessage(chatId, "Не удалось добавить введенные активы. Пожалуйста введите их в формате: TICKER1 QUANTITY (SBER 20 GAZP 15)"));
        }
    }

    private void changeUsersStockCommandReceiver(String chatId, String param) {
        try {
            userService.updateUserStock(chatId, new ChangeUsersStock("", botParamHandler.parseChangeStockParam(param)));
            executeMessage(new SendMessage(chatId, userService.getPortfolioInformation(chatId).toString()));
        } catch (TelegramBotException e) {
            executeMessage(new SendMessage(chatId, "Введенный актив не существует. Пожалуйста напишите корректный figi-идентификатор актива."));
        } catch (Exception e) {
            executeMessage(new SendMessage(chatId, "Не удалось изменить введенные активы. Пожалуйста введите их в формате: FIGI QUANTITY (BBG004730RP0 20 RU000A0JS6M0 15)"));
        }
    }

    private void deleteUsersStockCommandReceiver(String chatId, String param) {
        try {
            userService.updateUserStock(chatId, new ChangeUsersStock("", botParamHandler.parseDeleteStockParam(param)));
            executeMessage(new SendMessage(chatId, userService.getPortfolioInformation(chatId).toString()));
        } catch (Exception e) {
            executeMessage(new SendMessage(chatId, "Введенный актив не существует. Пожалуйста напишите корректный figi-идентификатор актива."));
        }
    }

    private void getCurrentCurrencyRate(String chatId, String date) {
        try {
            StringBuilder currencyRates = new StringBuilder("Доступные курсы валют(по отношению к рублю) на сегодня:\n");
            Set<CurrencyRateDto> currentCurrencyRates = new HashSet<>();
            if (date == null) {
                currentCurrencyRates = currencyService.getCurrentCurrencyRates();
            } else {
                try {
                    currentCurrencyRates = currencyService.getCurrentCurrencyRatesDateSpecified(date);
                } catch (TelegramBotException e) {
                    executeMessage(new SendMessage(chatId, "Что то пошло не так... Проверьте что дата соответствует шаблону: дд.мм.гггг"));
                    return;
                }
            }
            for (CurrencyRateDto rate : currentCurrencyRates) {
                currencyRates.append(rate.name()).append(" = ").append(rate.rate()).append(";\n");
            }
            executeMessage(new SendMessage(chatId, currencyRates.toString()));
        } catch (TelegramBotException e) {
            executeMessage(new SendMessage(chatId, "Что то пошло не так..."));
        }
    }

    private void findStockByTicker(String chatId, String ticker) {
        try {
            Set<FoundedStockDto> foundedStocks = stockSearchService.searchStockByTickerAndExchangeCode(ticker);
            if (foundedStocks.isEmpty()) {
                executeMessage(new SendMessage(chatId, "По данному запросу ничего не найдено, проверьте правильность введенного тикера."));
            } else {
                StringBuilder stocks = new StringBuilder("Результат поиска:\n");
                for (FoundedStockDto stock : foundedStocks) {
                    stocks.append("{").append("Название актива: ").append(stock.name())
                            .append("\n").append("FIGI: ").append(stock.figi())
                            .append("\n").append("Тикер: ").append(stock.ticker())
                            .append("\n").append("Exchange code: ").append(stock.exchCode())
                            .append("}\n");
                }
                executeMessage(new SendMessage(chatId, stocks.toString()));
            }
        } catch (Exception e) {
            executeMessage(new SendMessage(chatId, "По данному запросу ничего не найдено, проверьте правильность введенного тикера."));
        }
    }

    private void executeMessage(SendMessage msg) {
        try {
            msg.enableMarkdown(true);
            execute(msg);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
