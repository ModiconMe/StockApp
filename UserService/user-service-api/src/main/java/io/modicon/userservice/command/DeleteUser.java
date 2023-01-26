package io.modicon.userservice.command;

import io.modicon.cqrsbus.Command;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteUser implements Command<DeleteUserResult> {

    @NotEmpty(message = "user id should be provided")
    private String id;

}
