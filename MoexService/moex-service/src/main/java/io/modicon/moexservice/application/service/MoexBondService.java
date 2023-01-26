package io.modicon.moexservice.application.service;

import io.modicon.moexservice.infrastructure.exception.ApiException;
import io.modicon.moexservice.application.client.CorporateBondsClient;
import io.modicon.moexservice.application.client.GovBondsClient;
import io.modicon.moexservice.domain.model.Bond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoexBondService {

    private final CorporateBondsClient corporateBondsClient;
    private final GovBondsClient govBondsClient;
    private final BondParser parser;

    @Cacheable(value = "corps")
    public List<Bond> getCorporateBonds() {
        log.info("Getting corporate bonds from MOEX");
        String xmlFromMoex = corporateBondsClient.getBondsFromMoex();
        List<Bond> corporateBonds = parser.parse(xmlFromMoex);
        if (corporateBonds.isEmpty()) {
            log.error("MOEX isn't answering for getting corporate bonds");
            throw ApiException.exception(HttpStatus.TOO_MANY_REQUESTS, "MOEX isn't answering for getting corporate bonds");
        }
        return corporateBonds;
    }

    @Cacheable(value = "govs")
    public List<Bond> getGovBonds() {
        log.info("Getting government bonds from MOEX");
        String xmlFromMoex = govBondsClient.getBondsFromMoex();
        List<Bond> govBonds = parser.parse(xmlFromMoex);
        if (govBonds.isEmpty()) {
            log.error("MOEX isn't answering for getting government bonds");
            throw ApiException.exception(HttpStatus.TOO_MANY_REQUESTS, "MOEX isn't answering for getting government bonds");
        }
        return govBonds;
    }
}
