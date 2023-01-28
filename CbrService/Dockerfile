FROM openjdk:17-jdk-alpine
COPY cbr-service/build/libs/cbr-service.jar .
EXPOSE 8006
ENTRYPOINT ["java", "-jar", "cbr-service.jar"]