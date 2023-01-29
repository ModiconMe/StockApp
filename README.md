> ### Stock App

### Highlights
- Приложение построено на основе CQRS и Микросервисов
- FeignClient для взаимодействия между микросервисами
- Микросервис состоит из 2 модулей (api, service)

### Technology
- Spring Boot 3.0.1 и Java 17
- Spring Data JPA + PosgreSQL
- Spring Data Redis
- Spring OpenFeign
- Logback
- JUnit 5 + AssertJ для тестирования
- Docker
- GitHub actions в качестве CI

### Getting started
Требуется Java 17 или выше

    ./gradlew bootRun

Или запустить с помощью Docker

    docker compose up

### Telegram Bot

Доступные команды

* /help
* /addstocks
* /updatestock
* /deletestock
* /currenciesrates
* /currenciesratesdate
* /portfoliocost
* /portfolioclassstatictic
* /findstocksbyticker

