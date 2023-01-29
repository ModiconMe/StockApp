package io.modicon.openfigiservice.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.application.StockMapper;
import io.modicon.openfigiservice.application.client.OpenFigiClient;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerResponse;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenFigiStockService {

    private final OpenFigiClient openFigiClient;

    public List<FoundedStockDto> getStockByTicker(List<GetTickerRequest> request) {
        log.info("get ticker from open figi {}", request);

        List<GetTickerResponse> stockByTicker = openFigiClient.getStockByTicker(request);
        List<FoundedStockDto> result = new ArrayList<>();
        if (!stockByTicker.isEmpty()) {
            log.info("get response from open figi {}", stockByTicker);
            for (GetTickerResponse response : stockByTicker) {
                if (response.error() != null) {
                    log.warn("open figi response error: " + response.error());
                }
                if (response.data() != null) {
                    result.add(StockMapper.mapToDto(response.data().get(0)));
                }
            }
        }

        return result;
    }

    public List<FoundedStockDto> searchStockByTicker(SearchTickerRequest request) {
        log.info("get ticker from open figi {}", request);

        SearchTickerResponse searchTickerResponse = openFigiClient.searchStockByTicker(request);
        log.info("get response from open figi {}", searchTickerResponse);
        List<FoundedStockDto> result = new ArrayList<>();
        if (searchTickerResponse.error() != null) {
            log.warn("open figi search response error: " + searchTickerResponse.error());
        }
        if (searchTickerResponse.data() != null) {
            result = searchTickerResponse.data().stream().map(StockMapper::mapToDto).toList();
        }

        return result;
    }

}
