package io.modicon.moexservice.application.client;

import io.modicon.moexservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "corporate-bonds",
        url = "${moex.bonds.corporate.url}",
        configuration = ApplicationConfig.class)
public interface CorporateBondsClient {
    @GetMapping
    String getBondsFromMoex();
}
