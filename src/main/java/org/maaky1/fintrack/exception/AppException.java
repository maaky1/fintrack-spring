package org.maaky1.fintrack.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private HttpStatus httpStatus;
    private String code;
    private String message;

    public AppException(HttpStatus httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
