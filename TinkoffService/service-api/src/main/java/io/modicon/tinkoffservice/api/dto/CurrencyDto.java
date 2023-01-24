package io.modicon.tinkoffservice.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyDto {
    rub("RUB"),
    usd("USD"),
    eur("EUR"),
    gbp("GBP"),
    hkd("HKD"),
    chf("CHF"),
    jpy("JPY"),
    cny("CNY");

    private final String currency;
}
