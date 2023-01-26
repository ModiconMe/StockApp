FROM openjdk:17-jdk-alpine
COPY moex-service/build/libs/price-service.jar .
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "price-service.jar"]