package io.modicon.moexservice.application;

import io.modicon.moexservice.domain.model.Bond;
import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;

public class BondMapper {
    public static StockDto mapToDto(Bond bond) {
        return StockDto.builder()
                .ticker(bond.ticker())
                .name(bond.name())
                .figi(bond.ticker())
                .type("Bond")
                .currency(CurrencyDto.RUB)
                .source("MOEX")
                .build();
    }
}
