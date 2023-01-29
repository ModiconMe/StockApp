package io.modicon.telegrambot.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class TelegramBotExceptionDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is4xxClientError()) {
            return new TelegramBotException(responseBody.toString());
        } else if (responseStatus.is5xxServerError()) {
            return new TelegramBotException("Что-то пошло не так");
        } else {
            return new Exception("Ошибка...");
        }
    }
}
