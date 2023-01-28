package io.modicon.userservice.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCodeResult;
import io.modicon.userservice.application.client.OpenFigiServiceClient;
import io.modicon.userservice.domain.model.PositionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TickerFigiConverterServiceTest {

    @Mock
    private OpenFigiServiceClient openFigiServiceClient;

    private TickerFigiConverterService tickerFigiConverterService;

    @BeforeEach
    void setUp() {
        tickerFigiConverterService = new TickerFigiConverterService(openFigiServiceClient);
    }

    private PositionEntity positionToAdd1 = new PositionEntity("ticker1", 1);
    private PositionEntity positionToAdd2 = new PositionEntity("figi2", 2);
    private PositionEntity positionToAdd3 = new PositionEntity("ticker3", 3);
    private PositionEntity positionToAdd4 = new PositionEntity("figi4", 4);
    private Set<PositionEntity> validPositionsToAdd = new HashSet<>();

    private Set<SearchStockDto> searchStockDtos = new HashSet<>();
    private SearchStockDto searchedStock1 = new SearchStockDto(positionToAdd1.getFigi(), "RX");
    private SearchStockDto searchedStock2 = new SearchStockDto(positionToAdd2.getFigi(), "RX");
    private SearchStockDto searchedStock3 = new SearchStockDto(positionToAdd3.getFigi(), "RX");
    private SearchStockDto searchedStock4 = new SearchStockDto(positionToAdd4.getFigi(), "RX");

    private List<FoundedStockDto> foundedStockDtoList = new ArrayList<>();
    private FoundedStockDto foundedStockDto1 = new FoundedStockDto("figi1", positionToAdd1.getFigi(), "RX", "name1");

    private PositionEntity resultPosition1 = new PositionEntity("figi1", 1);
    private PositionEntity resultPosition2 = new PositionEntity("figi2", 2);
    private PositionEntity resultPosition3 = new PositionEntity("ticker3", 3);
    private PositionEntity resultPosition4 = new PositionEntity("figi4", 4);
    private List<PositionEntity> resultPostionList = new ArrayList<>();
    {
        validPositionsToAdd.add(positionToAdd1);
        validPositionsToAdd.add(positionToAdd2);
        validPositionsToAdd.add(positionToAdd3);
        validPositionsToAdd.add(positionToAdd4);

        searchStockDtos.add(searchedStock1);
        searchStockDtos.add(searchedStock2);
        searchStockDtos.add(searchedStock3);
        searchStockDtos.add(searchedStock4);

        foundedStockDtoList.add(foundedStockDto1);

        resultPostionList.add(resultPosition1);
        resultPostionList.add(resultPosition2);
        resultPostionList.add(resultPosition3);
        resultPostionList.add(resultPosition4);
    }

    @Test
    void should_replaceTickerToFigi() {
        when(openFigiServiceClient.getStock(any())).thenReturn(new GetStockByTickerAndCodeResult(foundedStockDtoList));
        List<PositionEntity> figisFromTickers = tickerFigiConverterService.getFigisFromTickers(validPositionsToAdd);

        assertThat(figisFromTickers).isEqualTo(resultPostionList);

    }
}