package io.modicon.userservice.application.service;

import io.modicon.stockservice.api.query.GetStocksWithPrices;
import io.modicon.stockservice.api.query.GetStocksWithPricesResult;
import io.modicon.userservice.application.StockMapper;
import io.modicon.userservice.application.client.StockServiceClient;
import io.modicon.userservice.domain.model.StockWithPrice;
import io.modicon.userservice.infrastructure.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StockWithPriceService {

    private final StockServiceClient stockServiceClient;

    public List<StockWithPrice> getStocksWithPrice(List<String> userFigis) {
        GetStocksWithPricesResult stockWithPrices = stockServiceClient.getStockWithPrices(new GetStocksWithPrices(userFigis));

        Set<String> notFoundFigis = stockWithPrices.getNotFoundFigis();
        if (!notFoundFigis.isEmpty())
            throw ApiException.exception(HttpStatus.NOT_FOUND, "there some figis that not exists anymore", notFoundFigis);

        return stockWithPrices.getStocks().stream().map(StockMapper::mapToEntity).toList();
    }

}
