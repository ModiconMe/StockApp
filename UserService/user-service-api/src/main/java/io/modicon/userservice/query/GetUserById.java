package io.modicon.userservice.query;

import io.modicon.cqrsbus.Query;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetUserById implements Query<GetUserByIdResult> {

    @NotEmpty(message = "user id should be provided")
    private String id;

}
