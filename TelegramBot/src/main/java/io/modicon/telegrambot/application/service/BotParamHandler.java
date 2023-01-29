package io.modicon.telegrambot.application.service;

import io.modicon.userservice.dto.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class BotParamHandler {

    public Set<PositionDto> parseAddStockParam(String param) {
        Set<PositionDto> positions = new HashSet<>();
        String[] params = param.split(" ");
        for (int i = 0; i < params.length - 1; i++) {
            String figi = "";
            int quantity = 0;
            if (i % 2 == 0) {
                figi = params[i];
                quantity = Integer.parseInt(params[i + 1]);
            }
            positions.add(new PositionDto(figi, quantity, ""));
        }
        return positions;
    }

    public PositionDto parseChangeStockParam(String param) {
        String[] params = param.split(" ");
        return new PositionDto(params[0], Integer.parseInt(params[1]), "");
    }

    public PositionDto parseDeleteStockParam(String param) {
        return new PositionDto(param, 0, "");
    }

}
