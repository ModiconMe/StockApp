package io.modicon.priceservice.application;

import io.modicon.priceservice.domain.model.StockWithPrice;
import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;

import java.math.BigDecimal;

public class StockMapper {
    public static StockWithPriceDto mapToStockWithPricesDto(StockWithPrice stock) {
        return StockWithPriceDto.builder()
                .ticker(stock.ticker())
                .figi(stock.figi())
                .name(stock.name())
                .type(stock.type())
                .currency(CurrencyDto.valueOf(stock.currency().getCurrency()))
                .source(stock.source())
                .price(stock.price())
                .build();
    }

    public static StockWithPrice mapToStockWithPrice(StockWithPriceDto stockDto) {
        return StockWithPrice.builder()
                .ticker(stockDto.ticker())
                .figi(stockDto.figi())
                .name(stockDto.name())
                .type(stockDto.type())
                .currency(CurrencyDto.valueOf(stockDto.currency().getCurrency()))
                .source(stockDto.source())
                .price(stockDto.price())
                .build();
    }

    public static StockWithPrice updatePrice(StockWithPrice stock, BigDecimal price) {
        return StockWithPrice.builder()
                .ticker(stock.ticker())
                .figi(stock.figi())
                .name(stock.name())
                .type(stock.type())
                .currency(CurrencyDto.valueOf(stock.currency().getCurrency()))
                .source(stock.source())
                .price(price)
                .build();
    }
}
