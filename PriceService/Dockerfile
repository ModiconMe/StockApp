FROM openjdk:17-jdk-alpine
COPY price-service/build/libs/price-service.jar .
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "price-service.jar"]