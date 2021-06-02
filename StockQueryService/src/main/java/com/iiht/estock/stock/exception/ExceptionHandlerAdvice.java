package com.iiht.estock.stock.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class ExceptionHandlerAdvice {

    @ExceptionHandler({ StockNotFoundException.class, NoSuchElementException.class })
    public ResponseEntity<ExceptionResponseMessage> handleControllerExceptionNotFound(final HttpServletRequest request,
                                                                                      final Throwable ex) {
        log.error("Exception type: " + ex.getClass());
        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ExceptionResponseMessage.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ExceptionResponseMessage> handleControllerExceptionFieldValidation(MethodArgumentNotValidException ex) {
        log.error("Exception type: " + ex.getClass());
        log.error(ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> error:
                errors.entrySet()){
            builder.append(error.getKey()).append(" : ").append(error.getValue()).append(";");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ExceptionResponseMessage.builder().message(builder.toString()).build());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ExceptionResponseMessage> handleGenericException(final HttpServletRequest request,
                                                                           final Throwable ex) {
        log.error("Exception type: " + ex.getClass());
        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ExceptionResponseMessage.builder().message("some error occurred.").build());
    }
}
