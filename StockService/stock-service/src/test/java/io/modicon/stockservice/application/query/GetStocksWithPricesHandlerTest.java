package io.modicon.stockservice.application.query;

import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.stockservice.application.EntitySource;
import io.modicon.stockservice.application.service.MoexStockPriceService;
import io.modicon.stockservice.application.service.TinkoffStockPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetStocksWithPricesHandlerTest {

    @Mock
    private TinkoffStockPriceService tinkoffStockPriceService;
    @Mock
    private MoexStockPriceService moexStockPriceService;

    private GetStocksWithPricesHandler getStocksWithPricesHandler;
    private EntitySource entitySource = new EntitySource();

    @BeforeEach
    void setUp() {
        getStocksWithPricesHandler = new GetStocksWithPricesHandler(tinkoffStockPriceService, moexStockPriceService);
    }

    @Test
    void should_returnCorrectData_whenOnlyTinkoffStocks() {
        when(tinkoffStockPriceService.getStocksWithPrices(any()))
                .thenReturn(entitySource.stocksWithPrice);

        GetStocksWithPricesResult result = getStocksWithPricesHandler.handle(new GetStocksWithPrices(entitySource.figis));

        Set<StockWithPriceDto> stocks = new HashSet<>(result.getStocks());
        assertThat(stocks).isNotEmpty();
        assertThat(stocks.size()).isEqualTo(entitySource.stocksWithPrice.size());
        assertThat(stocks).isEqualTo(entitySource.stocksWithPrice);
    }

    @Test
    void should_returnCorrectData_whenOnlyMoexStocks() {
        when(tinkoffStockPriceService.getStocksWithPrices(any()))
                .thenReturn(new HashSet<>());
        when(moexStockPriceService.getStocksWithPrices(any()))
                .thenReturn(entitySource.stocksWithPrice);

        GetStocksWithPricesResult result = getStocksWithPricesHandler.handle(new GetStocksWithPrices(entitySource.figis));

        Set<StockWithPriceDto> stocks = new HashSet<>(result.getStocks());
        assertThat(stocks).isNotEmpty();
        assertThat(stocks.size()).isEqualTo(entitySource.stocksWithPrice.size());
        assertThat(stocks).isEqualTo(entitySource.stocksWithPrice);
    }
}