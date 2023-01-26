FROM openjdk:17-jdk-alpine
COPY stock-service/build/libs/tinkoff-service.jar .
EXPOSE 8004
ENTRYPOINT ["java", "-jar", "tinkoff-service.jar"]