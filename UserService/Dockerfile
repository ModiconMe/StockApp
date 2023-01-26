FROM openjdk:17-jdk-alpine
COPY moex-service/build/libs/user-service.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]