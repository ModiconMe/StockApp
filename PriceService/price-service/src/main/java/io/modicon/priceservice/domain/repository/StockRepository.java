package io.modicon.priceservice.domain.repository;

import io.modicon.priceservice.domain.model.StockWithPrice;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface StockRepository extends KeyValueRepository<StockWithPrice, String> {
}
