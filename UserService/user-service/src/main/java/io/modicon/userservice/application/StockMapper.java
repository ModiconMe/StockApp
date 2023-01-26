package io.modicon.userservice.application;

import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.userservice.domain.model.Currency;
import io.modicon.userservice.domain.model.StockEntity;
import io.modicon.userservice.domain.model.StockWithPrice;
import io.modicon.userservice.domain.model.TypeEntity;

public class StockMapper {
    public static StockEntity mapToEntity(StockDto stockDto) {
        return StockEntity.builder()
                .figi(stockDto.figi())
                .ticker(stockDto.ticker())
                .name(stockDto.name())
                .type(TypeEntity.valueOf(stockDto.type().toUpperCase()))
                .currency(Currency.valueOf(stockDto.currency().getCurrency().toUpperCase()))
                .build();
    }

    public static StockWithPrice mapToEntity(StockWithPriceDto stockWithPriceDto) {
        return StockWithPrice.builder()
                .figi(stockWithPriceDto.figi())
                .ticker(stockWithPriceDto.ticker())
                .name(stockWithPriceDto.name())
                .type(TypeEntity.valueOf(stockWithPriceDto.type().toUpperCase()))
                .currency(Currency.valueOf(stockWithPriceDto.currency().getCurrency().toUpperCase()))
                .price(stockWithPriceDto.price())
                .build();
    }
}
