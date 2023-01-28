package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.model.CurrencyRate;

import java.util.Set;

public interface CurrencyParser {
    Set<CurrencyRate> parse(String ratesAsString);
}
