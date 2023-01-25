FROM openjdk:17-jdk-alpine
COPY stock-service/build/libs/stock-service.jar .
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "service.jar"]