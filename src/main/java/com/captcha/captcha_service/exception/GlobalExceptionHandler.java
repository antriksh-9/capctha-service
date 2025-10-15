package com.captcha.captcha_service.exception;

import com.captcha.captcha_service.model.CaptchaValidateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CaptchaNotFoundException.class)
    public ResponseEntity<CaptchaValidateResponse> handleNotFound(CaptchaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.GONE)
            .body(CaptchaValidateResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }
    
    @ExceptionHandler(CaptchaExpiredException.class)
    public ResponseEntity<CaptchaValidateResponse> handleExpired(CaptchaExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE)
            .body(CaptchaValidateResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CaptchaValidateResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CaptchaValidateResponse.builder()
                .success(false)
                .message("Internal server error")
                .build());
    }
}
