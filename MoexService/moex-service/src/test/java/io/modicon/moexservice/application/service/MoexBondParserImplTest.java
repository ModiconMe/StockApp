package io.modicon.moexservice.application.service;

import io.modicon.moexservice.application.service.BondParser;
import io.modicon.moexservice.application.service.MoexBondParserImpl;
import io.modicon.moexservice.domain.model.Bond;
import io.modicon.moexservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MoexBondParserImplTest {

    BondParser bondParser;

    private static final String VALID_XML =
            """
                    <document>
                    <data id="securities">
                    <rows>
                            <row SECID="AMUNIBB2DER6" PREVADMITTEDQUOTE="101.2" SHORTNAME="UBANK02/24"/>
                            <row SECID="RU000A0JQ7Z2" PREVADMITTEDQUOTE="99.82" SHORTNAME="РЖД-19 обл"/>
                            <row SECID="RU000A0JQAL8" PREVADMITTEDQUOTE="101.5" SHORTNAME="ДОМ.РФ14об"/>
                    </rows>
                    </data>
                    </document>
                    """;

    private static final String INVALID_XML =
            """
                    <document>
                    <rows>
                            <row SECID="AMUNIBB2DER6" PREVADMITTEDQUOTE="101.2" SHORTNAME="UBANK02/24"/>
                            <row SECID="RU000A0JQ7Z2" PREVADMITTEDQUOTE="99.82" SHORTNAME="РЖД-19 обл"/>
                            <row SECID="RU000A0JQAL8" PREVADMITTEDQUOTE="101.5" SHORTNAME="ДОМ.РФ14об"/>
                    </rows>
                    </data>
                    </document>
                    """;


    @BeforeEach
    void setUp() {
        bondParser = new MoexBondParserImpl();
    }

    @Test
    void should_parseXML_whenXMLisValid() {
        List<Bond> bonds = bondParser.parse(VALID_XML);
        assertThat(bonds).isNotEmpty();
        assertThat(bonds.size()).isEqualTo(3);

        Bond bond1 = new Bond("AMUNIBB2DER6", BigDecimal.valueOf(101.2), "UBANK02/24");
        Bond bond2 = new Bond("RU000A0JQ7Z2", BigDecimal.valueOf(99.82), "РЖД-19 обл");
        Bond bond3 = new Bond("RU000A0JQAL8", BigDecimal.valueOf(101.5), "ДОМ.РФ14об");
        assertThat(bonds).isEqualTo(List.of(bond1, bond2, bond3));
    }

    @Test
    void should_throw_whenXMLisInvalid() {
        assertThatThrownBy(() -> bondParser.parse(INVALID_XML))
                .isInstanceOf(ApiException.class);
    }
}