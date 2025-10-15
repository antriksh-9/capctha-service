package com.captcha.captcha_service.exception;

public class CaptchaNotFoundException extends RuntimeException {
    public CaptchaNotFoundException(String message) {
        super(message);
    }
}