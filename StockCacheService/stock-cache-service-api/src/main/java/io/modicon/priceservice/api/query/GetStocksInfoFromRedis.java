package io.modicon.priceservice.api.query;

import io.modicon.cqrsbus.Query;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetStocksInfoFromRedis implements Query<GetStocksInfoFromRedisResult> {

    private List<SearchStockDto> stocks;

}
