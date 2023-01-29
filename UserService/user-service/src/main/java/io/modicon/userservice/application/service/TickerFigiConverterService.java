package io.modicon.userservice.application.service;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import io.modicon.openfigiservice.api.query.GetStockByTickerAndCode;
import io.modicon.userservice.application.client.OpenFigiServiceClient;
import io.modicon.userservice.domain.model.PositionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TickerFigiConverterService {

    private final OpenFigiServiceClient openFigiServiceClient;

    public List<PositionEntity> getFigisFromTickers(Set<PositionEntity> requestedByTickerPositions) {
        List<String> tickers = requestedByTickerPositions.stream().map(PositionEntity::getFigi).toList();

        List<FoundedStockDto> foundedStock = openFigiServiceClient.getStock(new GetStockByTickerAndCode(
                tickers.stream().map(ticker -> new SearchStockDto(ticker, "RX")).collect(Collectors.toSet())
        )).getStocks();
        Map<String, String> figiTickerMap;

        List<PositionEntity> tickerReplacedToFigiPositions = new ArrayList<>();
        if (!foundedStock.isEmpty()) {
            figiTickerMap = foundedStock.stream().collect(Collectors.toMap(FoundedStockDto::ticker, FoundedStockDto::figi));
            for (PositionEntity position : requestedByTickerPositions) {
                String figi = figiTickerMap.get(position.getFigi());
                if (figi != null)
                    tickerReplacedToFigiPositions.add(new PositionEntity(figi, position.getQuantity()));
                else
                    tickerReplacedToFigiPositions.add(position);
            }
        } else {
            tickerReplacedToFigiPositions.addAll(requestedByTickerPositions); // if not found by ticker
        }

        return tickerReplacedToFigiPositions;
    }

}
