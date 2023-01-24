package io.modicon.moexservice.application.client;

import io.modicon.moexservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "government-bonds",
        url = "${moex.bonds.government.url}",
        configuration = ApplicationConfig.class)
public interface GovBondsClient {
    @GetMapping
    String getBondsFromMoex();
}
