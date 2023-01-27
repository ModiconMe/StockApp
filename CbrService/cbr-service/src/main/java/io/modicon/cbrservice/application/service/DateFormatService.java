package io.modicon.cbrservice.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.modicon.cbrservice.infrastructure.exception.ApiException.exception;

@Service
public class DateFormatService {

    private final String PATTERN = "dd/MM/yyyy";
    private final String SEPARATORS = "\\.|_|-";
    private final String VALID_SEPARATOR = "/";

    public String formatDate(String date) {
        String formattedDate = "";
        try {
            formattedDate = date.replaceAll(SEPARATORS, VALID_SEPARATOR);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
            LocalDate.parse(formattedDate, dateTimeFormatter);
        } catch (Exception e) {
            throw exception(HttpStatus.BAD_REQUEST, "cannot parse date %s, please make sure that your date match pattern dd.MM.yyyy", date);
        }
        return formattedDate;
    }
}
