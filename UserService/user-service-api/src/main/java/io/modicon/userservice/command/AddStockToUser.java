package io.modicon.userservice.command;

import io.modicon.cqrsbus.Command;
import io.modicon.userservice.dto.PositionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddStockToUser implements Command<AddStockToUserResult> {

    @With
    private String id;
    private Set<PositionDto> positions;

}
