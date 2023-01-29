package io.modicon.openfigiservice.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.application.client.OpenFigiClient;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerResponse;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerResponse;
import io.modicon.openfigiservice.domain.model.OpenFigiStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenFigiStockServiceTest {

    @Mock
    private OpenFigiClient openFigiClient;

    private OpenFigiStockService openFigiStockService;

    @BeforeEach
    void setUp() {
        openFigiStockService = new OpenFigiStockService(openFigiClient);
    }

    private GetTickerRequest getTickerRequest1 = new GetTickerRequest("TICKER", "ticker1", "RX");
    private GetTickerRequest getTickerRequest2 = new GetTickerRequest("TICKER", "ticker2", "RX");
    private GetTickerRequest getTickerRequest3 = new GetTickerRequest("TICKER", "ticker3", "RX");
    private GetTickerRequest getTickerRequest4 = new GetTickerRequest("TICKER", "NOTFOUND", "RX");
    private List<GetTickerRequest> requestTickers = new ArrayList<>();

    private FoundedStockDto foundedStock1 = new FoundedStockDto("figi1", "ticker1", "RX", "name1");
    private FoundedStockDto foundedStock2 = new FoundedStockDto("figi2", "ticker2", "RX", "name2");
    private FoundedStockDto foundedStock3 = new FoundedStockDto("figi3", "ticker3", "RX", "name3");
    private List<FoundedStockDto> foundedStocks = new ArrayList<>();

    private OpenFigiStock openFigiStock1 = new OpenFigiStock("figi1", "st", "share", "ticker1", "name1", "RX", "sc", "figi1", "st2", "sd");
    private OpenFigiStock openFigiStock2 = new OpenFigiStock("figi2", "st", "share", "ticker2", "name2", "RX", "sc", "figi2", "st2", "sd");
    private OpenFigiStock openFigiStock3 = new OpenFigiStock("figi3", "st", "share", "ticker3", "name3", "RX", "sc", "figi3", "st2", "sd");
    private GetTickerResponse tickerResponse1 = new GetTickerResponse(List.of(openFigiStock1), null, null);
    private GetTickerResponse tickerResponse2 = new GetTickerResponse(List.of(openFigiStock2), null, null);
    private GetTickerResponse tickerResponse3 = new GetTickerResponse(List.of(openFigiStock3), null, null);
    private List<GetTickerResponse> tickerResponses = new ArrayList<>();
    private List<GetTickerResponse> notFoundResponse = new ArrayList<>();

    private SearchTickerRequest searchTickerRequest = new SearchTickerRequest("ticker1", "RX");
    private SearchTickerResponse searchTickerResponse = new SearchTickerResponse(List.of(openFigiStock1, openFigiStock2, openFigiStock3), "", "");
    private SearchTickerResponse notFoundSearchResult = new SearchTickerResponse(null, null, null);
    {
        requestTickers.add(getTickerRequest1);
        requestTickers.add(getTickerRequest2);
        requestTickers.add(getTickerRequest3);
        requestTickers.add(getTickerRequest4);

        foundedStocks.add(foundedStock1);
        foundedStocks.add(foundedStock2);
        foundedStocks.add(foundedStock3);

        tickerResponses.add(tickerResponse1);
        tickerResponses.add(tickerResponse2);
        tickerResponses.add(tickerResponse3);
    }

    @Test
    void should_returnFoundedStockByTicker() {
        when(openFigiClient.getStockByTicker(requestTickers)).thenReturn(tickerResponses);
        List<FoundedStockDto> stockByTicker = openFigiStockService.getStockByTicker(requestTickers);

        assertThat(stockByTicker).isNotEmpty();
        assertThat(stockByTicker.size()).isEqualTo(foundedStocks.size());
        assertThat(stockByTicker).isEqualTo(foundedStocks);
    }

    @Test
    void should_returnEmptyList_whenNotFound() {
        when(openFigiClient.getStockByTicker(requestTickers)).thenReturn(notFoundResponse);
        List<FoundedStockDto> stockByTicker = openFigiStockService.getStockByTicker(requestTickers);

        assertThat(stockByTicker).isEmpty();
    }

    @Test
    void should_searchStockByTicker() {
        when(openFigiClient.searchStockByTicker(searchTickerRequest)).thenReturn(searchTickerResponse);
        List<FoundedStockDto> searchResult = openFigiStockService.searchStockByTicker(searchTickerRequest);

        assertThat(searchResult).isNotEmpty();
        assertThat(searchResult.size()).isEqualTo(foundedStocks.size());
        assertThat(searchResult).isEqualTo(foundedStocks);
    }

    @Test
    void should_returnEmptyList_whenSearchResultIsEmptyList() {
        when(openFigiClient.searchStockByTicker(searchTickerRequest)).thenReturn(notFoundSearchResult);
        List<FoundedStockDto> searchResult = openFigiStockService.searchStockByTicker(searchTickerRequest);

        assertThat(searchResult).isEmpty();
    }
}