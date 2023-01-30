FROM openjdk:17-jdk-alpine
COPY build/libs/TelegramBot.jar .
EXPOSE 80
ENTRYPOINT ["java", "-jar", "TelegramBot.jar"]