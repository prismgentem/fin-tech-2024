package org.example.crudkudago.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.HTML;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_FOUND"),
    CURRENCY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CURRENCY_NOT_FOUND"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE");

    private final int status;
    private final String code;
}
