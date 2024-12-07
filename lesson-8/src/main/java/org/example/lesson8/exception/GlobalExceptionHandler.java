package org.example.lesson8.exception;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Duration;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus())
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        var headers = new HttpHeaders();

        if (ex.getStatus() == 503) {
            Duration waitDuration = circuitBreakerRegistry
                    .circuitBreaker("cbrService")
                    .getCircuitBreakerConfig()
                    .getMaxWaitDurationInHalfOpenState();
            headers.add("Retry-After", String.valueOf(waitDuration.getSeconds()));
        }

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(ex.getStatus()));
    }
}

