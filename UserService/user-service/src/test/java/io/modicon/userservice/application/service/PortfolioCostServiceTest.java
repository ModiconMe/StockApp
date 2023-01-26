package io.modicon.userservice.application.service;

import io.modicon.userservice.domain.model.Currency;
import io.modicon.userservice.domain.model.StockWithPrice;
import io.modicon.userservice.domain.model.TypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PortfolioCostServiceTest {

    private CurrencyService currencyService;

    private PortfolioCostService portfolioCostService;

    @BeforeEach
    void setUp() {
        currencyService = new FakeCurrencyService();
        portfolioCostService = new PortfolioCostService(currencyService);
    }

    private BigDecimal rubCost = BigDecimal.valueOf(1);
    private BigDecimal usdCost = BigDecimal.valueOf(65);
    private BigDecimal eurCost = BigDecimal.valueOf(75);
    private BigDecimal gbpCost = BigDecimal.valueOf(86);
    private BigDecimal cnyCost = BigDecimal.valueOf(10);
    private BigDecimal chfCost = BigDecimal.valueOf(76);
    private BigDecimal hkdCost = BigDecimal.valueOf(99);
    private BigDecimal jpyCost = BigDecimal.valueOf(53);

    private List<StockWithPrice> stocks = new ArrayList<>();
    private StockWithPrice stock1 = new StockWithPrice("figi1", "figi1", "name1", TypeEntity.SHARE, Currency.EUR, "TINKOFF", BigDecimal.valueOf(10));
    private StockWithPrice stock2 = new StockWithPrice("figi2", "figi2", "name2", TypeEntity.SHARE, Currency.GBP, "TINKOFF", BigDecimal.valueOf(50));
    private StockWithPrice stock3 = new StockWithPrice("figi3", "figi3", "name3", TypeEntity.ETF, Currency.RUB, "TINKOFF", BigDecimal.valueOf(2000));
    private StockWithPrice stock4 = new StockWithPrice("figi4", "figi4", "name4", TypeEntity.BOND, Currency.USD, "MOEX", BigDecimal.valueOf(10));

    {
        stocks.add(stock1);
        stocks.add(stock2);
        stocks.add(stock3);
        stocks.add(stock4);
    }

    @Test
    void should_returnCostPortfolioInRub() {
        Map<String, BigDecimal> result = portfolioCostService.getPositionsCostInRub(stocks);

        assertThat(result.size()).isEqualTo(stocks.size());
        assertThat(result.get(stock1.figi())).isEqualTo(eurCost.multiply(stock1.price()));
        assertThat(result.get(stock2.figi())).isEqualTo(gbpCost.multiply(stock2.price()));
        assertThat(result.get(stock3.figi())).isEqualTo(rubCost.multiply(stock3.price()));
        assertThat(result.get(stock4.figi())).isEqualTo(usdCost.multiply(stock4.price()));
    }

}