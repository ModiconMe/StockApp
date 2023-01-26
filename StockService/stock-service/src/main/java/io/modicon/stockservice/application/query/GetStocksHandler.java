package io.modicon.stockservice.application.query;

import io.modicon.cqrsbus.QueryHandler;
import io.modicon.moexservice.api.query.GetMoexBonds;
import io.modicon.stockservice.api.query.GetStocks;
import io.modicon.stockservice.api.query.GetStocksResult;
import io.modicon.stockservice.application.StockMapper;
import io.modicon.stockservice.application.client.MoexServiceClient;
import io.modicon.stockservice.application.client.TinkoffServiceClient;
import io.modicon.stockservice.model.Stock;
import io.modicon.tinkoffservice.api.query.GetTinkoffStocks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetStocksHandler implements QueryHandler<GetStocksResult, GetStocks> {

    private final TinkoffServiceClient tinkoffService;
    private final MoexServiceClient moexService;

    @Override
    public GetStocksResult handle(GetStocks query) {

        List<String> figis = new ArrayList<>(query.getFigis().stream().map(String::trim).toList());
        List<Stock> resultList;

        log.info("get stocks from tinkoff-service");
        List<Stock> tinkoffStocks = tinkoffService.getStocks(new GetTinkoffStocks(figis)).getStocks()
                .stream().map(StockMapper::mapToStock).toList();
        List<String> figisFromTinkoff = tinkoffStocks.stream().map(Stock::figi).toList();
        figis.removeAll(figisFromTinkoff);
        log.info("successfully received stocks from tinkoff-service - {}", figisFromTinkoff);
        resultList = new ArrayList<>(tinkoffStocks);

        if (!figis.isEmpty()) {
            log.info("get stocks from moex-service");
            List<Stock> moexStocks = moexService.getBonds(new GetMoexBonds(figis)).getBonds()
                    .stream().map(StockMapper::mapToStock).toList();
            List<String> figisFromMoex = moexStocks.stream().map(Stock::figi).toList();
            figis.removeAll(figisFromMoex);
            log.info("successfully received stocks from moex-service - {}", figisFromMoex);
            resultList.addAll(moexStocks);
        }

        return new GetStocksResult(resultList.stream().map(StockMapper::mapToStockDto).toList(), new HashSet<>(figis));
    }

}
