package io.modicon.tinkoffservice.application.query;

import io.modicon.stockservice.api.dto.CurrencyDto;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockPriceDto;
import io.modicon.tinkoffservice.api.query.*;
import io.modicon.tinkoffservice.application.service.StockService;
import io.modicon.tinkoffservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryHandlersTest {

    @Mock
    StockService stockService;

    GetTinkoffStockHandler getStockHandler;
    GetTinkoffStocksHandler getStocksHandler;
    GetTinkoffPricesHandler getPricesHandler;

    static final String STOCK_NAME = "Test name";
    static final String STOCK_TICKER = "Test ticker";
    static final String STOCK_FIGI = "Test figi";
    static final String STOCK_TYPE = "Test type";
    static final String STOCK_SOURCE = "TINKOFF";
    static final CurrencyDto STOCK_CURR = CurrencyDto.rub;
    static final long STOCK_UNITS = 100;
    static final int STOCK_NANO = 50;
    static final BigDecimal STOCK_PRICE = BigDecimal.valueOf(100.50);

    final Instrument mockedInstrument = Instrument.newBuilder()
            .setName(STOCK_NAME)
            .setTicker(STOCK_TICKER)
            .setFigi(STOCK_FIGI)
            .setInstrumentType(STOCK_TYPE)
            .setCurrency(STOCK_CURR.getCurrency().toLowerCase())
            .build();

    final GetOrderBookResponse mockedOrderBook = GetOrderBookResponse.newBuilder()
            .setFigi(STOCK_FIGI)
            .setLastPrice(
                    Quotation.newBuilder()
                            .setUnits(STOCK_UNITS)
                            .setNano(STOCK_NANO)
                            .build()
            ).build();

    @BeforeEach
    void setUp() {
        getStockHandler = new GetTinkoffStockHandler(stockService);
        getStocksHandler = new GetTinkoffStocksHandler(stockService);
        getPricesHandler = new GetTinkoffPricesHandler(stockService);
    }

    // GetStockHandler
    @Test
    void should_returnCorrectData_whenStockExist() {
        when(stockService.getMarketInstrumentByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(mockedInstrument));

        GetTinkoffStockResult stock = getStockHandler.handle(new GetTinkoffStock(STOCK_FIGI));
        StockDto stockDto = stock.getStock();
        assertThat(stockDto.ticker()).isEqualTo(STOCK_TICKER);
        assertThat(stockDto.figi()).isEqualTo(STOCK_FIGI);
        assertThat(stockDto.name()).isEqualTo(STOCK_NAME);
        assertThat(stockDto.type()).isEqualTo(STOCK_TYPE);
        assertThat(stockDto.source()).isEqualTo(STOCK_SOURCE);
        assertThat(stockDto.currency()).isEqualTo(STOCK_CURR);
    }

    // GetStockHandler
    @Test
    void should_throw_whenStockNotExist() {
        when(stockService.getMarketInstrumentByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(null));

        assertThatThrownBy(() -> getStockHandler.handle(new GetTinkoffStock(STOCK_FIGI)))
                .isInstanceOf(ApiException.class);
    }

    // GetStocksHandler
    @Test
    void should_returnCorrectData_whenStocksExist() {
        when(stockService.getMarketInstrumentByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(mockedInstrument));

        GetTinkoffStocksResult stocks = getStocksHandler.handle(new GetTinkoffStocks(List.of(STOCK_FIGI)));
        StockDto stockDto = stocks.getStocks().get(0);
        assertThat(stocks.getStocks().size()).isEqualTo(1);
        assertThat(stockDto.ticker()).isEqualTo(STOCK_TICKER);
        assertThat(stockDto.figi()).isEqualTo(STOCK_FIGI);
        assertThat(stockDto.name()).isEqualTo(STOCK_NAME);
        assertThat(stockDto.type()).isEqualTo(STOCK_TYPE);
        assertThat(stockDto.source()).isEqualTo(STOCK_SOURCE);
        assertThat(stockDto.currency()).isEqualTo(STOCK_CURR);
    }

    // GetStocksHandler
    @Test
    void should_returnCorrectData_whenStocksNotExist() {
        when(stockService.getMarketInstrumentByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(null));

        GetTinkoffStocksResult stocks = getStocksHandler.handle(new GetTinkoffStocks(List.of(STOCK_FIGI)));
        assertThat(stocks.getStocks()).isEmpty();
    }

    // GetStockPricesHandler
    @Test
    void should_returnCorrectData_whenStockPricesExist() {
        when(stockService.getOrderBookByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(mockedOrderBook));

        GetTinkoffStockPricesResult stockPrices = getPricesHandler.handle(new GetTinkoffStockPrices(List.of(STOCK_FIGI)));
        StockPriceDto stockPriceDto = stockPrices.getStockPrices().get(0);
        assertThat(stockPrices.getStockPrices().size()).isEqualTo(1);
        assertThat(stockPriceDto.figi()).isEqualTo(STOCK_FIGI);
        assertThat(stockPriceDto.price()).isEqualTo(STOCK_PRICE);
    }

    // GetStockPricesHandler
    @Test
    void should_returnCorrectData_whenStockPricesNotExist() {
        when(stockService.getOrderBookByFigi(STOCK_FIGI))
                .thenReturn(CompletableFuture.completedFuture(null));

        GetTinkoffStockPricesResult stockPrices = getPricesHandler.handle(new GetTinkoffStockPrices(List.of(STOCK_FIGI)));
        assertThat(stockPrices.getStockPrices()).isEmpty();
    }
}