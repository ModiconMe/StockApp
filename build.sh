#/bin/bash
./gradlew clean build
docker build -t modiconme/stock-service:latest StockService/
docker build -t modiconme/tinkoff-service:latest TinkoffService/
docker build -t modiconme/moex-service:latest MoexService/
docker login -u $1 -p $2
docker push modiconme/stock-service:latest
docker push modiconme/tinkoff-service:latest
docker push modiconme/moex-service:latest

