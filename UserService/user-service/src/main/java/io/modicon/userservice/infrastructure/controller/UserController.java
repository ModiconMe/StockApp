package io.modicon.userservice.infrastructure.controller;

import io.modicon.cqrsbus.Bus;
import io.modicon.userservice.command.*;
import io.modicon.userservice.operation.UserOperation;
import io.modicon.userservice.query.GetUserById;
import io.modicon.userservice.query.GetUserByIdResult;
import io.modicon.userservice.query.GetUsers;
import io.modicon.userservice.query.GetUsersResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController implements UserOperation {

    private final Bus bus;

    @Override
    public CreateUserResult createUser(CreateUser cmd) {
        return bus.executeCommand(cmd);
    }

    @Override
    public GetUserByIdResult getUserById(String id) {
        return bus.executeQuery(new GetUserById(id));
    }

    @Override
    public AddStockToUserResult addStockToUser(String id, AddStockToUser cmd) {
        return bus.executeCommand(cmd.withId(id));
    }

    @Override
    public ChangeUsersStockResult changeUsersStock(String id, ChangeUsersStock cmd) {
        return bus.executeCommand(cmd.withId(id));
    }

    @Override
    public void deleteUser(String id) {
        bus.executeCommand(new DeleteUser(id));
    }

    @Override
    public GetUsersResult getUsers() {
        return bus.executeQuery(new GetUsers());
    }

}
