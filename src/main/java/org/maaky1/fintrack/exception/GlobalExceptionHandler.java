package org.maaky1.fintrack.exception;

import org.maaky1.fintrack.dto.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseInfo<?>> handleAppException(AppException e) {
        ResponseInfo<?> response = new ResponseInfo<>()
                .setCode(e.getCode())
                .setStatus((e.getHttpStatus().is5xxServerError()) ? "System Error" : "Failed")
                .setMessage(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseInfo<?>> handleBadCredentials(BadCredentialsException e) {
        ResponseInfo<?> response = new ResponseInfo<>()
                .setCode("1201")
                .setStatus("Failed")
                .setMessage("Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
