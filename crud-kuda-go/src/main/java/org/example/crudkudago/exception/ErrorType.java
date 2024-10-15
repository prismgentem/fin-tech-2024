package org.example.crudkudago.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BadRequest"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NotFound");

    private final int status;
    private final String code;
}
