package io.modicon.userservice.domain.repository;

import io.modicon.userservice.domain.model.StockEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StockRepository extends CrudRepository<StockEntity, String> {
    boolean existsByFigi(String figi);
    Optional<StockEntity> findByFigi(String figi);
}
