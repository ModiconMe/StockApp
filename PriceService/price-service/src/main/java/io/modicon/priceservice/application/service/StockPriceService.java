package io.modicon.priceservice.application.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface StockPriceService {

    Map<String, BigDecimal> getNewPrices(Set<String> figis);

}
