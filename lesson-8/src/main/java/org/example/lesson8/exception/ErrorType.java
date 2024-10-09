package org.example.lesson8.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    CURRENCY_NOT_FOUND(404, "CURRENCY_NOT_FOUND"),
    INVALID_REQUEST(400, "INVALID_REQUEST"),
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE");

    private final int status;
    private final String code;
}
