package com.captcha.captcha_service.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaData implements Serializable {

    private String question;
    private Integer answer;
    private Long expiryTime;
}
    

















