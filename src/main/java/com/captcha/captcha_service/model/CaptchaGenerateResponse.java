package com.captcha.captcha_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaGenerateResponse {
    private String captchaId;
    private String question;
    private Integer expiresIn;
    private String imageBase64; 
}
