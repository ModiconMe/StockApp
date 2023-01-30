package io.modicon.telegrambot.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import feign.FeignException;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.stockservice.api.dto.CurrencyRateDto;
import io.modicon.telegrambot.application.service.BotParamHandler;
import io.modicon.telegrambot.application.service.CurrencyService;
import io.modicon.telegrambot.application.service.StockSearchService;
import io.modicon.telegrambot.application.service.UserService;
import io.modicon.telegrambot.config.ApplicationConfig;
import io.modicon.telegrambot.config.TelegramBotException;
import io.modicon.userservice.command.AddStockToUser;
import io.modicon.userservice.command.AddStockToUserResult;
import io.modicon.userservice.command.ChangeUsersStock;
import io.modicon.userservice.command.CreateUser;
import io.modicon.userservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

@Component
@Slf4j
public final class Bot extends TelegramLongPollingBot {

    private final ApplicationConfig botConfig;
    private final UserService userService;
    private final BotParamHandler botParamHandler;
    private final CurrencyService currencyService;
    private final StockSearchService stockSearchService;
    private final ObjectMapper objectMapper;

    private static final String HELP_MESSAGE = """
            Я - бот-агрегатор акций, облигаций и курсов валют.
            Я помогу тебе найти нужные активы по идентефикаторам (Ticker или FIGI).
            Вот что я умею:
            /start - начало работы с ботом, регистрация пользователя по его chatId и username;
            /addstock - добавление актива по его идентификаторам (Ticker или FIGI). Данные вводятся в формате:
            TICKER1 QUANTITY1 FIGI2 QUANTITY2. Пример: SBER 20 GAZP 31 RU000A0JS6M0 16.
            /portfolioinfo - просмотр информации о портфеле пользователя.
            /updatestock - обновление количества актива в портфеле пользователя по FIGI.
            /deletestock - удаление актива из портфеля пользователя по FIGI.
            /currency - выводит курс валют на текущую дату.
            /currency 30.01.2022 - выводит курс валют на указанную дату.
            /findstock - поиск актива по его Ticker.
            """;

    public Bot(ApplicationConfig botConfig, UserService userService, BotParamHandler botParamHandler, CurrencyService currencyService, StockSearchService stockSearchService, ObjectMapper objectMapper) {
        this.botConfig = botConfig;
        this.userService = userService;
        this.botParamHandler = botParamHandler;
        this.currencyService = currencyService;
        this.stockSearchService = stockSearchService;
        this.objectMapper = objectMapper;
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
            String param = "";
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
                        executeMessage(chatId, HELP_MESSAGE);
                    }
                    case HELP -> executeMessage(chatId, HELP_MESSAGE);
                    case ADD_STOCK -> addStockCommandReceiver(chatId, param);
                    case DELETE_STOCK -> deleteUsersStockCommandReceiver(chatId, param);
                    case UPDATE_STOCK -> changeUsersStockCommandReceiver(chatId, param);
                    case GET_PORTFOLIO_INFO -> getPortfolioInfo(chatId);
                    case CURRENCIES_RATES -> getCurrentCurrencyRate(chatId, param);
                    case FIND_STOCK_BY_TICKER -> findStockByTicker(chatId, param);
                }
            } else {
                executeMessage(chatId, "Извините, я вас не понимаю. Попробуйте /help");
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
        String answer = EmojiParser.parseToUnicode(String.format("Привет, %s, приятно познакомится! :blush:", username));
        executeMessage(chatId, answer);
        log.info("Replied to user {}", username);
    }

    private void addStockCommandReceiver(String chatId, String param) {
        try {
            AddStockToUserResult addStockToUserResult = userService.addStockToUser(chatId, new AddStockToUser("", botParamHandler.parseAddStockParam(param)));
            if (!addStockToUserResult.getNotFoundFigis().isEmpty())
                executeMessage(chatId, "Не удалось найти:" + addStockToUserResult.getNotFoundFigis());
            executeMessage(chatId, userService.getPortfolioInformation(chatId).toString());
        } catch (FeignException e) {
                executeMessage(chatId, feignExceptionMapper(e));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeMessage(chatId, "Что то пошло не так... Проверьте правильность введенных данных /help.");
        }
    }

    private void getPortfolioInfo(String chatId) {
        try {
            executeMessage(chatId, userService.getPortfolioInformation(chatId).toString());
        } catch (FeignException e) {
            executeMessage(chatId, feignExceptionMapper(e));
        }
    }

    private void changeUsersStockCommandReceiver(String chatId, String param) {
        try {
            userService.updateUserStock(chatId, new ChangeUsersStock("", botParamHandler.parseChangeStockParam(param)));
            executeMessage(chatId, userService.getPortfolioInformation(chatId).toString());
        } catch (FeignException e) {
            executeMessage(chatId, feignExceptionMapper(e));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeMessage(chatId, "Что то пошло не так... Проверьте правильность введенных данных /help.");
        }
    }

    private void deleteUsersStockCommandReceiver(String chatId, String param) {
        try {
            userService.updateUserStock(chatId, new ChangeUsersStock("", botParamHandler.parseDeleteStockParam(param)));
            executeMessage(chatId, userService.getPortfolioInformation(chatId).toString());
        } catch (FeignException e) {
            executeMessage(chatId, feignExceptionMapper(e));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeMessage(chatId, "Что то пошло не так... Проверьте правильность введенных данных /help");
        }
    }

    private void getCurrentCurrencyRate(String chatId, String date) {
        String date1 = "сегодня";
        if (!date.equals("")) {
            date1 = date;
        }
        try {
            StringBuilder currencyRates = new StringBuilder(String.format("Доступные курсы валют(по отношению к рублю) на %s:\n", date1));
            Set<CurrencyRateDto> currentCurrencyRates;
            if (date.equals(""))
                currentCurrencyRates = currencyService.getCurrentCurrencyRates();
            else
                currentCurrencyRates = currencyService.getCurrentCurrencyRatesDateSpecified(date);
            for (CurrencyRateDto rate : currentCurrencyRates) {
                currencyRates.append(rate.name()).append(" = ").append(rate.rate()).append(";\n");
            }
            executeMessage(chatId, currencyRates.toString());
        } catch (FeignException e) {
            executeMessage(chatId, feignExceptionMapper(e));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeMessage(chatId, "Что то пошло не так... Проверьте правильность введенных данных /help");
        }
    }

    private void findStockByTicker(String chatId, String ticker) {
        try {
            Set<FoundedStockDto> foundedStocks = stockSearchService.searchStockByTickerAndExchangeCode(ticker);
            if (foundedStocks.isEmpty()) {
                executeMessage(chatId, "По данному запросу ничего не найдено, проверьте правильность введенного тикера.");
            } else {
                StringBuilder stocks = new StringBuilder("Результат поиска:\n");
                int resPos = 1;
                for (FoundedStockDto stock : foundedStocks) {
                    stocks.append(resPos++).append(". {").append("Название актива: ").append(stock.name())
                            .append("\n").append("FIGI: ").append(stock.figi())
                            .append("\n").append("Тикер: ").append(stock.ticker())
                            .append("\n").append("Exchange code: ").append(stock.exchCode())
                            .append("}\n");
                }
                executeMessage(chatId, stocks.toString());
            }
        } catch (FeignException e) {
            executeMessage(chatId, feignExceptionMapper(e));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeMessage(chatId, "Что то пошло не так... Проверьте правильность введенных данных /help");
        }
    }

    private void executeMessage(String chatId, String msg) {
            try {
                SendMessage sendMessage = new SendMessage(chatId, msg);
                sendMessage.enableMarkdown(true);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                executeMessage(chatId, "Что то пошло не так...");
            }
    }

    private String feignExceptionMapper(FeignException e) {
        try {
            return objectMapper.readValue(e.contentUTF8(), TelegramBotException.class).getMessage();
        } catch (JsonProcessingException ex) {
            log.error("Json exception parse error: " + ex.getMessage());
        }
        return "Что то пошло не так...";
    }

}
