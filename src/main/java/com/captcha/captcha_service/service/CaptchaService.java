package com.captcha.captcha_service.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.captcha.captcha_service.exception.CaptchaExpiredException;
import com.captcha.captcha_service.exception.CaptchaNotFoundException;

import com.captcha.captcha_service.model.*;

@Service
public class CaptchaService {

    private final MathCaptchaGenerator mathGenerator;
    private final CaptchaImageGenerator imageGenerator;
    private final CaptchaCacheService cacheService;

    @Value("${captcha.ttl:180}")
    private int ttl;

    public CaptchaService(MathCaptchaGenerator mathGenerator,
            CaptchaImageGenerator imageGenerator,
            CaptchaCacheService cacheService) {
        this.mathGenerator = mathGenerator;
        this.imageGenerator = imageGenerator;
        this.cacheService = cacheService;
    }

    public CaptchaGenerateResponse generateCaptcha(CaptchaGenerateRequest request) {
        String captchaId = UUID.randomUUID().toString();
        String difficulty = request.getDifficulty() != null ? request.getDifficulty() : "L1";

        CaptchaData captchaData = mathGenerator.generateCaptcha(difficulty);
        captchaData.setExpiryTime(System.currentTimeMillis() + (ttl * 1000L));

        cacheService.store(captchaId, captchaData);

        CaptchaGenerateResponse.CaptchaGenerateResponseBuilder responseBuilder = CaptchaGenerateResponse.builder()
                .captchaId(captchaId).question(captchaData.getQuestion()).expiresIn(ttl);

        if (Boolean.TRUE.equals(request.getAsImage())) {
            String imageBase64 = imageGenerator.generateImage(captchaData.getQuestion());
            responseBuilder.imageBase64(imageBase64);
        }
        return responseBuilder.build();
    }

    public CaptchaValidateResponse validateCaptcha(CaptchaValidateRequest request) {
        String captchaId = request.getCaptchaId();
        Integer userAnswer = request.getAnswer();

        if (!cacheService.exists(captchaId)) {
            throw new CaptchaNotFoundException("CAPTCHA not found or already used");
        }
        CaptchaData captchaData = cacheService.retrieve(captchaId);

        if (captchaData == null) {
            throw new CaptchaExpiredException("CAPTCHA has expired");
        }
        boolean isCorrect = captchaData.getAnswer().equals(userAnswer);

        if (isCorrect) {
            cacheService.delete(captchaId);
            return CaptchaValidateResponse.builder().success(true).message("CAPTCHA validation successful").build();
        } else {
            return CaptchaValidateResponse.builder().success(false).message("Incorrect answer").build();
        }
    }
}
