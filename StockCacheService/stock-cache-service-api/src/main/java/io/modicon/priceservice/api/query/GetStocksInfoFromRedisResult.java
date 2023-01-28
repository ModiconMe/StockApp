package io.modicon.priceservice.api.query;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStocksInfoFromRedisResult {

    private List<FoundedStockDto> stocks;

}
