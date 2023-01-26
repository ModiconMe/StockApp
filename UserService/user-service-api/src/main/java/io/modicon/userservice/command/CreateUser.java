package io.modicon.userservice.command;

import io.modicon.cqrsbus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUser implements Command<CreateUserResult> {

    private String id;
    private String name;

}
