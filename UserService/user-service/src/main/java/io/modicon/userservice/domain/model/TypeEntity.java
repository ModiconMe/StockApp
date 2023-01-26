package io.modicon.userservice.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TypeEntity {

    ETF("Etf"),
    SHARE("Share"),
    BOND("Bond");

    private final String value;

}
