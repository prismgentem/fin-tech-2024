package org.example.crudkudago.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String details;

    public ErrorResponse(String message, int status, String details) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }
}
