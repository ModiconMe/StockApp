package io.modicon.tinkoffservice.infrastructure.exception;

import io.modicon.tinkoffservice.api.dto.ApiExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(ApiException e) {
        return new ResponseEntity<>(new ApiExceptionDto(e.getMessage()), e.getStatus());
    }

    /**
     * Spring validation exception handling
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ApiExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Tinkoff api exceptions
     */
    @ExceptionHandler(value = ApiRuntimeException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestValidException(ApiRuntimeException e) {
        return new ResponseEntity<>(new ApiExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
