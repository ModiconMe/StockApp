package io.modicon.userservice.operation;

import io.modicon.userservice.command.*;
import io.modicon.userservice.query.*;
import org.springframework.web.bind.annotation.*;

public interface UserOperation {

    @PostMapping
    CreateUserResult createUser(@RequestBody CreateUser cmd);

    @GetMapping("/{id}")
    GetUserByIdResult getUserById(@PathVariable("id") String id);

    @PutMapping("/add-stock/{id}")
    AddStockToUserResult addStockToUser(@PathVariable("id") String id, @RequestBody AddStockToUser cmd);

    @PutMapping("/update-stock/{id}")
    ChangeUsersStockResult changeUsersStock(@PathVariable("id") String id, @RequestBody ChangeUsersStock cmd);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable("id") String id);

    @GetMapping
    GetUsersResult getUsers();

}
