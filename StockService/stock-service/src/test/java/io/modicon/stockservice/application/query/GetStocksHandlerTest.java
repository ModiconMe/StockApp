package io.modicon.stockservice.application.query;


import io.modicon.moexservice.api.query.GetMoexBondsResult;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.query.GetStocks;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.stockservice.application.EntitySource;
import io.modicon.stockservice.application.client.MoexServiceClient;
import io.modicon.stockservice.application.client.TinkoffServiceClient;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocksResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetStocksHandlerTest {

    @Mock
    private TinkoffServiceClient tinkoffService;
    @Mock
    private MoexServiceClient moexService;

    private GetStocksHandler getStocksHandler;
    private EntitySource entitySource = new EntitySource();

    @BeforeEach
    void setUp() {
        getStocksHandler = new GetStocksHandler(tinkoffService, moexService);
    }

    @Test
    void should_returnCorrectData_whenOnlyTinkoffStocks() {
        when(tinkoffService.getStocks(any()))
                .thenReturn(new GetTinkoffStocksResult(entitySource.stocks));

        GetStocksResult result = getStocksHandler.handle(new GetStocks(entitySource.figis));

        List<StockDto> stocks = result.getStocks();
        assertThat(stocks).isNotEmpty();
        assertThat(stocks.size()).isEqualTo(entitySource.stocks.size());
        assertThat(stocks).isEqualTo(entitySource.stocks);

        Set<String> notFoundFigis = result.getNotFoundFigis();
        assertThat(notFoundFigis).isEmpty();
    }

    @Test
    void should_returnCorrectData_whenOnlyMoexStocks() {
        when(tinkoffService.getStocks(any()))
                .thenReturn(new GetTinkoffStocksResult(new ArrayList<>()));
        when(moexService.getBonds(any()))
                .thenReturn(new GetMoexBondsResult(entitySource.stocks));

        GetStocksResult result = getStocksHandler.handle(new GetStocks(entitySource.figis));

        List<StockDto> stocks = result.getStocks();
        assertThat(stocks).isNotEmpty();
        assertThat(stocks.size()).isEqualTo(entitySource.stocks.size());
        assertThat(stocks).isEqualTo(entitySource.stocks);

        Set<String> notFoundFigis = result.getNotFoundFigis();
        assertThat(notFoundFigis).isEmpty();
    }

    @Test
    void should_returnCorrectData_whenHasNotFoundStocks() {
        when(tinkoffService.getStocks(any()))
                .thenReturn(new GetTinkoffStocksResult(new ArrayList<>()));
        when(moexService.getBonds(any()))
                .thenReturn(new GetMoexBondsResult(entitySource.stocks));

        GetStocksResult result = getStocksHandler.handle(new GetStocks(entitySource.figisWithNotExisted));

        List<StockDto> stocks = result.getStocks();
        assertThat(stocks).isNotEmpty();
        assertThat(stocks.size()).isEqualTo(entitySource.stocks.size());
        assertThat(stocks).isEqualTo(entitySource.stocks);

        Set<String> notFoundFigis = result.getNotFoundFigis();
        assertThat(notFoundFigis).isNotEmpty();
    }

}