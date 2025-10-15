package com.captcha.captcha_service.model;

import lombok.Data;

@Data
public class CaptchaGenerateRequest {
    private Boolean asImage = false;
    private String difficulty = "L1";
}
