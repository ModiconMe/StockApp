#/bin/bash
./gradlew clean build
docker build -t modiconme/stock-service:latest StockService/
docker build -t modiconme/tinkoff-service:latest TinkoffService/
docker build -t modiconme/moex-service:latest MoexService/
docker build -t modiconme/user-service:latest UserService/
docker build -t modiconme/stock-cache-service:latest StockCacheService/
docker build -t modiconme/cbr-service:latest CbrService/

