package org.example.lesson8.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    CURRENCY_NOT_FOUND(404, "CURRENCY_NOT_FOUND"),
    INVALID_REQUEST(400, "INVALID_REQUEST"),
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE"),
    INTERNAL_SERVER_ERROR(500, "SERVICE_ERROR");


    private final Integer status;
    private final String code;
}
