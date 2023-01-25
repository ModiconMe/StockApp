FROM openjdk:17-jdk-alpine
COPY moex-service/build/libs/moex-service.jar .
EXPOSE 8005
ENTRYPOINT ["java", "-jar", "moex-service.jar"]