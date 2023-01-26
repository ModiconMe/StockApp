package io.modicon.moexservice.api.query;

import io.modicon.stockservice.api.dto.StockPriceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetMoexBondPricesResult {

    private List<StockPriceDto> bondPrices;

}
