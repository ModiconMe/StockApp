package io.modicon.userservice.application;

import io.modicon.userservice.domain.model.PositionEntity;
import io.modicon.userservice.dto.PositionDto;

public class PositionMapper {
    public static PositionDto mapToDto(PositionEntity position) {
        return new PositionDto(position.getFigi(), position.getQuantity());
    }

    public static PositionEntity mapToEntity(PositionDto position) {
        return new PositionEntity(position.figi(), position.quantity());
    }
}
