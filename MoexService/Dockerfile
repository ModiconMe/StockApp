FROM openjdk:17-jdk-alpine
COPY service/build/libs/service.jar .
EXPOSE 8005
ENTRYPOINT ["java", "-jar", "service.jar"]