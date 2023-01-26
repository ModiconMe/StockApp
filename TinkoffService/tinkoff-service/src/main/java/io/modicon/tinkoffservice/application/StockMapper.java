package io.modicon.tinkoffservice.application;

import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;
import ru.tinkoff.piapi.contract.v1.Instrument;

public class StockMapper {
    public static StockDto mapToDto(Instrument instrument) {
        return StockDto.builder()
                .ticker(instrument.getTicker())
                .figi(instrument.getFigi())
                .name(instrument.getName())
                .type(instrument.getInstrumentType())
                .currency(CurrencyDto.valueOf(instrument.getCurrency().toUpperCase()))
                .source("TINKOFF")
                .build();
    }
}
