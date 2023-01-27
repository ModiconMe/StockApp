package io.modicon.stockcacheservice.domain.repository;

import io.modicon.stockcacheservice.domain.model.StockWithPrice;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface StockRepository extends KeyValueRepository<StockWithPrice, String> {
}
