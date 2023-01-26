package io.modicon.userservice.command;

import io.modicon.cqrsbus.Command;
import io.modicon.userservice.dto.PositionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangeUsersStock implements Command<ChangeUsersStockResult> {

    @With
    private String id;
    private PositionDto position;

}
