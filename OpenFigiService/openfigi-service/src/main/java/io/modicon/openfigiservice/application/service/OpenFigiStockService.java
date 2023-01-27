package io.modicon.openfigiservice.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.application.StockMapper;
import io.modicon.openfigiservice.application.client.OpenFigiClient;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.GetTickerResponse;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerRequest;
import io.modicon.openfigiservice.application.client.openfigi.SearchTickerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.modicon.openfigiservice.infrastructure.exception.ApiException.exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenFigiStockService {

    private final OpenFigiClient openFigiClient;
    private final ObjectMapper objectMapper;

    public List<FoundedStockDto> getStockByTicker(List<GetTickerRequest> request) {
        log.info("get ticker from open figi {}", request);

        List<GetTickerResponse> stockByTicker = openFigiClient.getStockByTicker(request);
        if (!stockByTicker.isEmpty()) {
            GetTickerResponse getTickerResponse = stockByTicker.get(0);
            log.info("get response from open figi {}", getTickerResponse);
            if (getTickerResponse.error() != null) {
                throw exception(HttpStatus.BAD_REQUEST, "error occurred from open figi %s", getTickerResponse.error());
            }
            if (getTickerResponse.data() != null) {
                return getTickerResponse.data().stream().map(StockMapper::mapToDto).toList();
            }
        }

        return null;
    }

    public List<FoundedStockDto> searchStockByTicker(SearchTickerRequest request) {
        log.info("get ticker from open figi {}", request);

        SearchTickerResponse searchTickerResponse = openFigiClient.searchStockByTicker(request);
        log.info("get response from open figi {}", searchTickerResponse);
        if (searchTickerResponse.error() != null) {
            throw exception(HttpStatus.BAD_REQUEST, "error occurred from open figi %s", searchTickerResponse.error());
        }
        if (searchTickerResponse.data() != null) {
            return searchTickerResponse.data().stream().map(StockMapper::mapToDto).toList();
        }

        return null;
    }

}
