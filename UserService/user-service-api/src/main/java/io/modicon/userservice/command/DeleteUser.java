package io.modicon.userservice.command;

import io.modicon.cqrsbus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteUser implements Command<DeleteUserResult> {

    private String id;

}
