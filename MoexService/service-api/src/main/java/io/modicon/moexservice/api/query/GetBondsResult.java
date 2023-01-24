package io.modicon.moexservice.api.query;

import io.modicon.moexservice.api.dto.BondDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetBondsResult {

    private List<BondDto> bonds;

}
