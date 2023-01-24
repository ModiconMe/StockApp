package io.modicon.moexservice.application;

import io.modicon.moexservice.api.dto.CurrencyDto;
import io.modicon.moexservice.api.dto.BondDto;
import io.modicon.moexservice.domain.model.Bond;

public class BondMapper {
    public static BondDto mapToDto(Bond bond) {
        return BondDto.builder()
                .ticker(bond.ticker())
                .name(bond.name())
                .figi(bond.ticker())
                .type("Bond")
                .currency(CurrencyDto.rub)
                .source("MOEX")
                .build();
    }
}
