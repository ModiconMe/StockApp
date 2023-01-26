package io.modicon.userservice.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDto(String id, String name, Set<PositionDto> stocks) {
}
