package io.modicon.userservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Setter
@Entity
@Table(indexes = {
        @Index(name = "figi_index", columnList = "figi"),
        @Index(name = "ticker_index", columnList = "ticker")
})
public class StockEntity {
    @EqualsAndHashCode.Include
    @Id
    private UUID id;
    private String ticker;
    private String figi;
    private Currency currency;
    private String name;
    private TypeEntity type;
}
