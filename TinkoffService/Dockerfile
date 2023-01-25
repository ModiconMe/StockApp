FROM openjdk:17-jdk-alpine
COPY tinkoff-service/build/libs/tinkoff-service.jar .
EXPOSE 8004
ENTRYPOINT ["java", "-jar", "service.jar"]