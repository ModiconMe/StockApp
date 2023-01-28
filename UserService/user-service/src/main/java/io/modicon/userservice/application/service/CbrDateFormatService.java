package io.modicon.userservice.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.modicon.userservice.infrastructure.exception.ApiException.exception;

@Service("cbrDateFormatService")
public class CbrDateFormatService implements DateFormatService {

    private final String PATTERN = "dd.MM.yyyy";
    private final String SEPARATORS = "\\.|_|-";
    private final String VALID_SEPARATOR = ".";

    @Override
    public String formatDate(String date) {
        String formattedDate;
        LocalDate parsedDate;
        try {
            formattedDate = date.replaceAll(SEPARATORS, VALID_SEPARATOR);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
            parsedDate = LocalDate.parse(formattedDate, dateTimeFormatter);
        } catch (Exception e) {
            throw exception(HttpStatus.BAD_REQUEST, "cannot parse date %s, please make sure that your date match pattern dd.MM.yyyy", date);
        }
        if (parsedDate.isBefore(LocalDate.of(1997, 1, 1)))
            throw exception(HttpStatus.BAD_REQUEST, "sorry, we don't have any information on %s, please enter date that is after 01.01.1997", date);
        return formattedDate;
    }
}
