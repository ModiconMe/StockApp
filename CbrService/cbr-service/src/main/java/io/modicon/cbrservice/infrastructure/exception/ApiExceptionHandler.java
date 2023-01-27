package io.modicon.cbrservice.infrastructure.exception;

import io.modicon.stockservice.api.dto.ApiExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiExceptionDto> handleApiRequestException(ApiException e) {
        return new ResponseEntity<>(new ApiExceptionDto(e.getMessage()), e.getStatus());
    }

}
