FROM openjdk:17-jdk-alpine
COPY stock-cache-service/build/libs/stock-cache-service.jar .
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "stock-cache-service.jar"]