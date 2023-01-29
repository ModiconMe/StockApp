package io.modicon.telegrambot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotCommandEnum {
    START("/start"),
    HELP("/help"),
    ADD_STOCK("/addstock"),
    UPDATE_STOCK("/updatestock"),
    DELETE_STOCK("/deletestock"),
    CURRENCIES_RATES("/currenciesrates"),
    GET_PORTFOLIO_INFO("/portfolioinfo"),
    FIND_STOCK_BY_TICKER("/findstockbyticker");

    private final String commandId;

    public static BotCommandEnum getCommand(String cmd) {
        BotCommandEnum botCommand = null;
        BotCommandEnum[] availableCommands = BotCommandEnum.values();
        for (BotCommandEnum command : availableCommands) {
            if (command.getCommandId().equals(cmd)) {
                botCommand = command;
                break;
            }
        }
        return botCommand;
    }
}
