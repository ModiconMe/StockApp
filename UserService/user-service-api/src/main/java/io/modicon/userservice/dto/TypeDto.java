package io.modicon.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TypeDto {

    @JsonProperty("Etf")
    ETF("Etf"),

    @JsonProperty("Share")
    SHARE("Share"),

    @JsonProperty("Bond")
    BOND("Bond");

    private final String value;

}
