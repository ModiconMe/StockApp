package io.modicon.stockservice.application;

import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.model.Currency;
import io.modicon.stockservice.model.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EntitySource {
    public List<String> figis = new ArrayList<>();
    public List<String> figisWithNotExisted = new ArrayList<>();
    List<Stock> stocksList = new ArrayList<>();
    public List<StockDto> stocks = new ArrayList<>();
    public List<StockPriceDto> stocksPrices = new ArrayList<>();
    public List<StockWithPriceDto> stocksWithPrice = new ArrayList<>();

    Stock stock1 = new Stock("ticker1", "ticker1", "name1", "type1", Currency.rub, "source1");
    Stock stock2 = new Stock("ticker2", "ticker2", "name2", "type2", Currency.usd, "source2");
    Stock stock3 = new Stock("ticker3", "ticker3", "name3", "type3", Currency.chf, "source3");
    Stock stock4 = new Stock("ticker4", "ticker4", "name4", "type4", Currency.cny, "source4");
    StockDto stockDto1 = new StockDto("ticker1", "ticker1", "name1", "type1", CurrencyDto.rub, "source1");
    StockDto stockDto2 = new StockDto("ticker2", "ticker2", "name2", "type2", CurrencyDto.usd, "source2");
    StockDto stockDto3 = new StockDto("ticker3", "ticker3", "name3", "type3", CurrencyDto.chf, "source3");
    StockDto stockDto4 = new StockDto("ticker4", "ticker4", "name4", "type4", CurrencyDto.cny, "source4");
    StockPriceDto stockPrice1 = new StockPriceDto("ticker1", BigDecimal.valueOf(100.5));
    StockPriceDto stockPrice2 = new StockPriceDto("ticker2", BigDecimal.valueOf(150.5));
    StockPriceDto stockPrice3 = new StockPriceDto("ticker3", BigDecimal.valueOf(50.5));
    StockPriceDto stockPrice4 = new StockPriceDto("ticker4", BigDecimal.valueOf(150.5));
    StockWithPriceDto stockWithPrice1 = new StockWithPriceDto("ticker1", "ticker1", "name1", "type1", CurrencyDto.rub, "source1", BigDecimal.valueOf(100.5));
    StockWithPriceDto stockWithPrice2 = new StockWithPriceDto("ticker2", "ticker2", "name2", "type2", CurrencyDto.usd, "source2", BigDecimal.valueOf(150.5));
    StockWithPriceDto stockWithPrice3 = new StockWithPriceDto("ticker3", "ticker3", "name3", "type3", CurrencyDto.chf, "source3", BigDecimal.valueOf(50.5));
    StockWithPriceDto stockWithPrice4 = new StockWithPriceDto("ticker4", "ticker4", "name4", "type4", CurrencyDto.cny, "source4", BigDecimal.valueOf(150.5));

    {
        stocksList.add(stock1);
        stocksList.add(stock2);
        stocksList.add(stock3);
        stocksList.add(stock4);
        figis.add("ticker1");
        figis.add("ticker2");
        figis.add("ticker3");
        figis.add("ticker4");
        figisWithNotExisted.add("ticker1");
        figisWithNotExisted.add("ticker2");
        figisWithNotExisted.add("ticker3");
        figisWithNotExisted.add("ticker4");
        figisWithNotExisted.add("notExisted");
        stocks.add(stockDto1);
        stocks.add(stockDto2);
        stocks.add(stockDto3);
        stocks.add(stockDto4);
        stocksPrices.add(stockPrice1);
        stocksPrices.add(stockPrice2);
        stocksPrices.add(stockPrice3);
        stocksPrices.add(stockPrice4);
        stocksWithPrice.add(stockWithPrice1);
        stocksWithPrice.add(stockWithPrice2);
        stocksWithPrice.add(stockWithPrice3);
        stocksWithPrice.add(stockWithPrice4);
    }
}
