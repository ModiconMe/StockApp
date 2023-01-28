FROM openjdk:17-jdk-alpine
COPY openfigi-service/build/libs/openfigi-service.jar .
EXPOSE 8007
ENTRYPOINT ["java", "-jar", "openfigi-service.jar"]