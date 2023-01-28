package io.modicon.stockcacheservice.domain.repository;

import io.modicon.stockcacheservice.domain.model.StockInfo;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.Optional;

public interface StockInfoRepository extends KeyValueRepository<StockInfo, String> {
    Optional<StockInfo> findByTicker(String ticker);
}
