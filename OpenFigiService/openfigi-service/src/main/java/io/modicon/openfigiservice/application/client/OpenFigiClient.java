package io.modicon.openfigiservice.application.client;

import io.modicon.openfigiservice.application.client.openfigi.GetTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerResponse;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerResponse;
import io.modicon.openfigiservice.infrastructure.config.ApplicationConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "open-figi-stock",
        url = "${api.openFigiConfig.openFigi}",
        configuration = ApplicationConfig.class)
public interface OpenFigiClient {
    @PostMapping("/mapping")
    List<GetTickerResponse> getStockByTicker(@RequestBody List<GetTickerRequest> request);
    @PostMapping("/search")
    SearchTickerResponse searchStockByTicker(@RequestBody SearchTickerRequest request);
}
