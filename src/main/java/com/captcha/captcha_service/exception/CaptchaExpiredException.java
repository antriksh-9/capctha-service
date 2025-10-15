package com.captcha.captcha_service.exception;

public class CaptchaExpiredException extends RuntimeException {
    public CaptchaExpiredException(String message) {
        super(message);
    }
}
