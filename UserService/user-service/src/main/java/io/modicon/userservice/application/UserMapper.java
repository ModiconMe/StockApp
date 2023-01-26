package io.modicon.userservice.application;

import io.modicon.userservice.domain.model.UserEntity;
import io.modicon.userservice.dto.PositionDto;
import io.modicon.userservice.dto.UserDto;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToDto(UserEntity user) {
        Set<PositionDto> positions = user.getStocks().stream()
                .map(PositionMapper::mapToDto).collect(Collectors.toSet());
        return new UserDto(user.getId(), user.getName(),
                positions);
    }
}
