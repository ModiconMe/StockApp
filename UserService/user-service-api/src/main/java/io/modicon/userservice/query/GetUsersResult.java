package io.modicon.userservice.query;

import io.modicon.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetUsersResult {

    private List<UserDto> users;

}
