package io.modicon.priceservice.api.query;

import io.modicon.cqrsbus.Command;
import io.modicon.openfigiservice.api.dto.FoundedStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PutStocksInfoToRedis implements Command<PutStocksInfoToRedisResult> {

    private List<FoundedStockDto> stocks;

}
