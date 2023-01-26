package io.modicon.userservice.command;

import io.modicon.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddStockToUserResult {

    private UserDto user;
    private Set<String> notFoundFigis;

}
