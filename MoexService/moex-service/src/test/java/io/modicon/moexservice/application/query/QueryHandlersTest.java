package io.modicon.moexservice.application.query;

import io.modicon.moexservice.api.query.GetMoexBondPrices;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.moexservice.application.query.GetMoexBondPricesHandler;
import io.modicon.moexservice.application.query.GetMoexBondsHandler;
import io.modicon.moexservice.application.service.MoexBondService;
import io.modicon.moexservice.domain.model.Bond;
import io.modicon.stockservice.api.dto.StockDto;
import io.modicon.stockservice.api.dto.StockPriceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryHandlersTest {

    @Mock
    MoexBondService bondService;

    GetMoexBondPricesHandler getBondPricesHandler;
    GetMoexBondsHandler getBondsHandler;

    List<Bond> bonds;

    {
        bonds = new ArrayList<>();
        bonds.add(new Bond("AMUNIBB2DER6", BigDecimal.valueOf(101.2), "UBANK02/24"));
        bonds.add(new Bond("RU000A0JQ7Z2", BigDecimal.valueOf(99.82), "РЖД-19 обл"));
        bonds.add(new Bond("RU000A0JQAL8", BigDecimal.valueOf(101.5), "ДОМ.РФ14об"));
    }

    @BeforeEach
    void setUp() {
        getBondPricesHandler = new GetMoexBondPricesHandler(bondService);
        getBondsHandler = new GetMoexBondsHandler(bondService);
    }

    @Test
    void should_returnCorrectData_whenGetBonds() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<StockDto> result = getBondsHandler.handle(new GetMoexBonds(List.of("AMUNIBB2DER6", "RU000A0JQ7Z2", "test"))).getBonds();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void should_returnCorrectData_whenBondsNotFound() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<StockDto> result = getBondsHandler.handle(new GetMoexBonds(List.of("test"))).getBonds();

        assertThat(result).isEmpty();
    }

    @Test
    void should_returnCorrectData_whenGetPrices() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<StockPriceDto> result = getBondPricesHandler.handle(new GetMoexBondPrices(List.of("AMUNIBB2DER6", "RU000A0JQ7Z2"))).getBondPrices();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void should_notThrow_whenBondPricesNotFound() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        getBondPricesHandler.handle(new GetMoexBondPrices(List.of("test")));
    }
}