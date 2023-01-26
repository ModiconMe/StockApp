package io.modicon.userservice.command;

import io.modicon.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateUserResult {

    private UserDto user;

}
