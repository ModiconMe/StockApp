package io.modicon.stockservice.application.service;

import io.modicon.moexservice.api.query.GetMoexBondPricesResult;
import io.modicon.moexservice.api.query.GetMoexBondsResult;
import io.modicon.stockservice.api.dto.StockWithPriceDto;
import io.modicon.stockservice.application.EntitySource;
import io.modicon.stockservice.application.client.MoexServiceClient;
import io.modicon.stockservice.application.client.TinkoffServiceClient;
import io.modicon.tinkoffservice.api.query.GetTinkoffStockPricesResult;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocksResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockPriceServiceTest {


    @Mock
    private MoexServiceClient moexServiceClient;
    @Mock
    private TinkoffServiceClient tinkoffServiceClient;
    private MoexStockPriceService moexStockPriceService;
    private TinkoffStockPriceService tinkoffStockPriceService;

    private EntitySource entitySource = new EntitySource();

    @BeforeEach
    void setUp() {
        moexStockPriceService = new MoexStockPriceService(moexServiceClient);
        tinkoffStockPriceService = new TinkoffStockPriceService(tinkoffServiceClient);
    }

    @Test
    void moex_should_returnCorrectData_whenTickersExist() {
        when(moexServiceClient.getBonds(any()))
                .thenReturn(new GetMoexBondsResult(entitySource.stocks));
        when(moexServiceClient.getPricesByFigis(any()))
                .thenReturn(new GetMoexBondPricesResult(entitySource.stocksPrices));

        Set<StockWithPriceDto> result = moexStockPriceService.getStocksWithPrices(entitySource.figis);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(entitySource.stocksWithPrice.size());
        assertThat(result).isEqualTo(entitySource.stocksWithPrice);

        assertThat(entitySource.figis).isEmpty();
    }

    @Test
    void tinkoff_should_returnCorrectData_whenTickersExist() {
        when(tinkoffServiceClient.getStocks(any()))
                .thenReturn(new GetTinkoffStocksResult(entitySource.stocks));
        when(tinkoffServiceClient.getPrices(any()))
                .thenReturn(new GetTinkoffStockPricesResult(entitySource.stocksPrices));

        Set<StockWithPriceDto> result = tinkoffStockPriceService.getStocksWithPrices(entitySource.figis);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(entitySource.stocksWithPrice.size());
        assertThat(result).isEqualTo(entitySource.stocksWithPrice);

        assertThat(entitySource.figis).isEmpty();
    }

    @Test
    void tinkoff_should_returnCorrectData_whenTickersIsNotExist() {
        when(tinkoffServiceClient.getStocks(any()))
                .thenReturn(new GetTinkoffStocksResult(new ArrayList<>()));
        when(tinkoffServiceClient.getPrices(any()))
                .thenReturn(new GetTinkoffStockPricesResult(new ArrayList<>()));

        Set<StockWithPriceDto> result = tinkoffStockPriceService.getStocksWithPrices(entitySource.figis);

        assertThat(result).isEmpty();

        assertThat(entitySource.figis).isNotEmpty();
    }
}