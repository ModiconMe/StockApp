package io.modicon.userservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Setter
@Entity
public class UserEntity {

        @EqualsAndHashCode.Include
        @Id
        private String id;
        private String name;

        @Singular
        @OneToMany(
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.EAGER
        )
        @JoinColumn(name = "stock_id", referencedColumnName = "id")
        private Set<PositionEntity> stocks;

}
