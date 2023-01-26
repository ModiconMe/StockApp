package io.modicon.userservice.application.service;

import io.modicon.userservice.domain.model.Currency;

import java.math.BigDecimal;
import java.util.Map;

public interface CurrencyService {

    Map<Currency, BigDecimal> getCurrencyCost();

}
