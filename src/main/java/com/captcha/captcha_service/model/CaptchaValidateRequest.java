package com.captcha.captcha_service.model;

import lombok.Data;

@Data
public class CaptchaValidateRequest {
    private String captchaId;
    private Integer answer;
}
