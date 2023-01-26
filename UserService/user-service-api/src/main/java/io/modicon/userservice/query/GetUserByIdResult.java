package io.modicon.userservice.query;

import io.modicon.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetUserByIdResult {

    private UserDto user;

}
