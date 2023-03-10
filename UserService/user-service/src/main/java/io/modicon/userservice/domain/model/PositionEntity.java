package io.modicon.userservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
public class PositionEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @EqualsAndHashCode.Include
        private String figi;
        private Integer quantity;
        private String name;

        public PositionEntity(String figi, Integer quantity) {
                this.figi = figi;
                this.quantity = quantity;
        }

        public PositionEntity(String figi, Integer quantity, String name) {
                this.figi = figi;
                this.quantity = quantity;
                this.name = name;
        }
}
