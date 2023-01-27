package io.modicon.openfigiservice.application;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import io.modicon.openfigiservice.domain.model.OpenFigiStock;

public class StockMapper {
    public static FoundedStockDto mapToDto(OpenFigiStock stock) {
        return FoundedStockDto.builder()
                .figi(stock.compositeFIGI())
                .ticker(stock.ticker())
                .name(stock.name())
                .exchCode(stock.exchCode())
                .build();
    }
}
