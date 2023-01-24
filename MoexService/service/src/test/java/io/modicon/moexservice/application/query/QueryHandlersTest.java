package io.modicon.moexservice.application.query;

import io.modicon.moexservice.api.dto.BondDto;
import io.modicon.moexservice.api.dto.BondPriceDto;
import io.modicon.moexservice.api.query.GetBondPrices;
import io.modicon.moexservice.api.query.GetBonds;
import io.modicon.moexservice.application.service.BondService;
import io.modicon.moexservice.domain.model.Bond;
import io.modicon.moexservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryHandlersTest {

    @Mock
    BondService bondService;

    GetBondPricesHandler getBondPricesHandler;
    GetBondsHandler getBondsHandler;

    List<Bond> bonds;

    {
        bonds = new ArrayList<>();
        bonds.add(new Bond("AMUNIBB2DER6", BigDecimal.valueOf(101.2), "UBANK02/24"));
        bonds.add(new Bond("RU000A0JQ7Z2", BigDecimal.valueOf(99.82), "РЖД-19 обл"));
        bonds.add(new Bond("RU000A0JQAL8", BigDecimal.valueOf(101.5), "ДОМ.РФ14об"));
    }

    @BeforeEach
    void setUp() {
        getBondPricesHandler = new GetBondPricesHandler(bondService);
        getBondsHandler = new GetBondsHandler(bondService);
    }

    @Test
    void should_returnCorrectData_whenGetBonds() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<BondDto> result = getBondsHandler.handle(new GetBonds(List.of("AMUNIBB2DER6", "RU000A0JQ7Z2", "test"))).getBonds();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void should_returnCorrectData_whenBondsNotFound() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<BondDto> result = getBondsHandler.handle(new GetBonds(List.of("test"))).getBonds();

        assertThat(result).isEmpty();
    }

    @Test
    void should_returnCorrectData_whenGetPrices() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        List<BondPriceDto> result = getBondPricesHandler.handle(new GetBondPrices(List.of("AMUNIBB2DER6", "RU000A0JQ7Z2"))).getBondPrices();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void should_throw_whenBondPricesNotFound() {
        when(bondService.getCorporateBonds()).thenReturn(bonds);
        when(bondService.getGovBonds()).thenReturn(bonds);

        assertThatThrownBy(() -> getBondPricesHandler.handle(new GetBondPrices(List.of("test"))))
                .isInstanceOf(ApiException.class);
    }
}