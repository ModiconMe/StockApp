package io.modicon.openfigiservice.api.query;

import io.modicon.cqrsbus.Query;
import io.modicon.openfigiservice.api.dto.SearchStockDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetStockByTickerAndCode implements Query<GetStockByTickerAndCodeResult> {

    private Set<SearchStockDto> stocks;

}
