#/bin/bash
./gradlew clean build
docker build -t modiconme/stock-service:latest StockService/
docker build -t modiconme/tinkoff-service:latest TinkoffService/
docker build -t modiconme/moex-service:latest MoexService/
docker build -t modiconme/user-service:latest UserService/
docker build -t modiconme/stock-cache-service:latest StockCacheService/
docker build -t modiconme/cbr-service:latest CbrService/
docker build -t modiconme/openfigi-service:latest OpenFigiService/
docker login -u $1 -p $2
docker push modiconme/stock-service:latest
docker push modiconme/tinkoff-service:latest
docker push modiconme/moex-service:latest
docker push modiconme/user-service:latest
docker push modiconme/stock-cache-service:latest
docker push modiconme/cbr-service:latest
docker push modiconme/openfigi-service:latest

