package io.modicon.userservice.query;

import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchStockInfoByTickerResult {
    private List<FoundedStockDto> stocks;
}
