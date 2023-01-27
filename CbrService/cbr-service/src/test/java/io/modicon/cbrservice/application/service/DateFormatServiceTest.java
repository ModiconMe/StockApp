package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@ExtendWith(MockitoExtension.class)
class DateFormatServiceTest {

    private DateFormatService dateFormatService;

    @BeforeEach
    void setUp() {
        dateFormatService = new DateFormatService();
    }

    @Test
    void should_correctParseDate_whenValidDateProvided() {
        String date = "20.02.2010";
        String expected = "20/02/2010";
        String formatDate = dateFormatService.formatDate(date);
        System.out.println(formatDate);
        assertThat(formatDate).isEqualTo(expected);
    }

    @Test
    void should_throw_whenProvidedDateInvalid() {
        String date = "10.24.2010";
        ApiException exception = catchThrowableOfType(() -> dateFormatService.formatDate(date), ApiException.class);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}