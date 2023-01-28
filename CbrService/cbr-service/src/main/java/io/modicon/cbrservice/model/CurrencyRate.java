package io.modicon.cbrservice.model;

import java.math.BigDecimal;

public record CurrencyRate(String name, String ticker, BigDecimal rate) {
}
