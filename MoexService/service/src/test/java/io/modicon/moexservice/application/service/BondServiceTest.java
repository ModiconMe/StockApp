package io.modicon.moexservice.application.service;

import io.modicon.moexservice.application.client.CorporateBondsClient;
import io.modicon.moexservice.application.client.GovBondsClient;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BondServiceTest {

    BondService bondService;

    @Mock
    CorporateBondsClient corporateBondsClient;
    @Mock
    GovBondsClient govBondsClient;
    @Mock
    BondParser parser;

    List<Bond> bonds;

    {
        bonds = new ArrayList<>();
        bonds.add(new Bond("AMUNIBB2DER6", BigDecimal.valueOf(101.2), "UBANK02/24"));
        bonds.add(new Bond("RU000A0JQ7Z2", BigDecimal.valueOf(99.82), "РЖД-19 обл"));
        bonds.add(new Bond("RU000A0JQAL8", BigDecimal.valueOf(101.5), "ДОМ.РФ14об"));
    }

    @BeforeEach
    void setUp() {
        bondService = new BondService(corporateBondsClient, govBondsClient, parser);
    }

    @Test
    void should_returnCorrectData_whenRequestForCorporateBonds() {
        String moexAnswer = bonds.toString();
        when(corporateBondsClient.getBondsFromMoex()).thenReturn(moexAnswer);
        when(parser.parse(moexAnswer)).thenReturn(bonds);

        List<Bond> result = bondService.getCorporateBonds();
        assertThat(result).isEqualTo(bonds);
    }

    @Test
    void should_throw_whenTooManyRequestToMoexCorporate() {
        String moexAnswer = bonds.toString();
        when(corporateBondsClient.getBondsFromMoex()).thenReturn(moexAnswer);
        when(parser.parse(moexAnswer)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> bondService.getCorporateBonds())
                .isInstanceOf(ApiException.class);
    }

    @Test
    void should_returnCorrectData_whenRequestForGovBonds() {
        String moexAnswer = bonds.toString();
        when(govBondsClient.getBondsFromMoex()).thenReturn(moexAnswer);
        when(parser.parse(moexAnswer)).thenReturn(bonds);

        List<Bond> result = bondService.getGovBonds();
        assertThat(result).isEqualTo(bonds);
    }

    @Test
    void should_throw_whenTooManyRequestToMoexGov() {
        String moexAnswer = bonds.toString();
        when(govBondsClient.getBondsFromMoex()).thenReturn(moexAnswer);
        when(parser.parse(moexAnswer)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> bondService.getGovBonds())
                .isInstanceOf(ApiException.class);
    }
}