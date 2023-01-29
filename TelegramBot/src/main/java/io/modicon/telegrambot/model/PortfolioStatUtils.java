package io.modicon.telegrambot.model;

import io.modicon.userservice.dto.PositionDto;
import io.modicon.userservice.dto.TypeDto;
import io.modicon.userservice.dto.UserDto;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class PortfolioStatUtils {
    private UserDto user;
    private BigDecimal totalCost;
    private Map<TypeDto, BigDecimal> percentMap;

    public static String positionsListBuilder(Set<PositionDto> positionDto) {
        StringBuilder positions = new StringBuilder();
        for (PositionDto position : positionDto) {
            positions.append("*")
                    .append(position.figi())
                    .append("(").append(position.name()).append(")")
                    .append("*")
                    .append(" : ")
                    .append(position.quantity()).append(" штук;");
            positions.append("\n");
        }
        return positions.toString();
    }

    public static String percentMapBuilder(Map<TypeDto, BigDecimal> percentMap) {
        StringBuilder percents = new StringBuilder();
        for (TypeDto type : percentMap.keySet()) {
            percents.append("*").append(type.getValue().toUpperCase()).append("*").append(" : ").append(percentMap.get(type)).append(" %;");
            percents.append("\n");
        }
        return percents.toString();
    }

    @Override
    public String toString() {
        String positions = positionsListBuilder(user.stocks());
        String percents = percentMapBuilder(percentMap);
        return String.format("Данные для пользователя с *id:%s*, *username:%s*", user.id(), user.name()) + "\n" +
                "Портфель состоит из следующих активов:" + "\n" +
                positions +
                "Распределение по активам:\n" + percents + "\n" +
        String.format("*Полная стоимость портфеля = %s рублей*", totalCost);
    }
}
