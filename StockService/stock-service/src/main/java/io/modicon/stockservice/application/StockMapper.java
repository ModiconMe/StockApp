package io.modicon.stockservice.application;

import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.model.Currency;
import io.modicon.stockservice.model.Stock;

import java.math.BigDecimal;

public class StockMapper {
    public static StockDto mapToStockDto(Stock stock) {
        return StockDto.builder()
                .ticker(stock.ticker())
                .figi(stock.figi())
                .name(stock.name())
                .type(stock.type())
                .currency(CurrencyDto.valueOf(stock.currency().getCurrency().toLowerCase()))
                .source(stock.source())
                .build();
    }

    public static Stock mapToStock(StockDto stockDto) {
        return Stock.builder()
                .ticker(stockDto.ticker())
                .figi(stockDto.figi())
                .name(stockDto.name())
                .type(stockDto.type())
                .currency(Currency.valueOf(stockDto.currency().getCurrency().toLowerCase()))
                .source(stockDto.source())
                .build();
    }

    public static StockWithPriceDto mapToStockWithPricesDto(Stock stock, BigDecimal price) {
        return StockWithPriceDto.builder()
                .ticker(stock.ticker())
                .figi(stock.figi())
                .name(stock.name())
                .type(stock.type())
                .currency(CurrencyDto.valueOf(stock.currency().getCurrency().toLowerCase()))
                .source(stock.source())
                .price(price)
                .build();
    }
}
