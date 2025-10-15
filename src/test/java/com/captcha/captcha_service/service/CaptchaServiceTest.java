package com.captcha.captcha_service.service;

import com.captcha.captcha_service.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CaptchaServiceTest {
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private CaptchaCacheService cacheService;
    
    @Test
    void testGenerateCaptcha() {
        CaptchaGenerateRequest request = new CaptchaGenerateRequest();
        request.setDifficulty("L1");
        request.setAsImage(false);
        
        CaptchaGenerateResponse response = captchaService.generateCaptcha(request);
        
        assertNotNull(response.getCaptchaId());
        assertNotNull(response.getQuestion());
        assertEquals(180, response.getExpiresIn());
    }
    
    @Test
    void testValidateCaptchaCorrect() {
        CaptchaGenerateRequest genRequest = new CaptchaGenerateRequest();
        genRequest.setDifficulty("L1");
        genRequest.setAsImage(false);
        
        CaptchaGenerateResponse genResponse = captchaService.generateCaptcha(genRequest);
        
        // Get the correct answer from cache instead of parsing
        CaptchaData cachedData = cacheService.retrieve(genResponse.getCaptchaId());
        assertNotNull(cachedData, "CAPTCHA should be in cache");
        
        int correctAnswer = cachedData.getAnswer();
        
        CaptchaValidateRequest valRequest = new CaptchaValidateRequest();
        valRequest.setCaptchaId(genResponse.getCaptchaId());
        valRequest.setAnswer(correctAnswer);
        
        CaptchaValidateResponse valResponse = captchaService.validateCaptcha(valRequest);
        
        assertTrue(valResponse.getSuccess(), "Validation should succeed with correct answer");
        assertEquals("CAPTCHA validation successful", valResponse.getMessage());
    }
    
    @Test
    void testValidateCaptchaIncorrect() {
        CaptchaGenerateRequest genRequest = new CaptchaGenerateRequest();
        genRequest.setDifficulty("L1");
        genRequest.setAsImage(false);
        
        CaptchaGenerateResponse genResponse = captchaService.generateCaptcha(genRequest);
        
        // Use wrong answer
        CaptchaValidateRequest valRequest = new CaptchaValidateRequest();
        valRequest.setCaptchaId(genResponse.getCaptchaId());
        valRequest.setAnswer(99999); // Intentionally wrong
        
        CaptchaValidateResponse valResponse = captchaService.validateCaptcha(valRequest);
        
        assertFalse(valResponse.getSuccess(), "Validation should fail with wrong answer");
        assertEquals("Incorrect answer", valResponse.getMessage());
    }
    
    @Test
    void testGenerateCaptchaWithImage() {
        CaptchaGenerateRequest request = new CaptchaGenerateRequest();
        request.setDifficulty("L2");
        request.setAsImage(true);
        
        CaptchaGenerateResponse response = captchaService.generateCaptcha(request);
        
        assertNotNull(response.getCaptchaId());
        assertNotNull(response.getQuestion());
        assertNotNull(response.getImageBase64(), "Image should be generated");
        assertTrue(response.getImageBase64().length() > 0, "Image data should not be empty");
    }
    
    @Test
    void testCaptchaOneTimeUse() {
        CaptchaGenerateRequest genRequest = new CaptchaGenerateRequest();
        genRequest.setDifficulty("L1");
        
        CaptchaGenerateResponse genResponse = captchaService.generateCaptcha(genRequest);
        
        // Get correct answer
        CaptchaData cachedData = cacheService.retrieve(genResponse.getCaptchaId());
        int correctAnswer = cachedData.getAnswer();
        
        // First validation - should succeed
        CaptchaValidateRequest valRequest = new CaptchaValidateRequest();
        valRequest.setCaptchaId(genResponse.getCaptchaId());
        valRequest.setAnswer(correctAnswer);
        
        CaptchaValidateResponse firstResponse = captchaService.validateCaptcha(valRequest);
        assertTrue(firstResponse.getSuccess());
        
        // Second validation with same ID - should fail (one-time use)
        assertThrows(Exception.class, () -> {
            captchaService.validateCaptcha(valRequest);
        }, "CAPTCHA should be deleted after first successful validation");
    }
    
    @Test
    void testDifferentDifficultyLevels() {
        // Test L1
        CaptchaGenerateRequest l1Request = new CaptchaGenerateRequest();
        l1Request.setDifficulty("L1");
        CaptchaGenerateResponse l1Response = captchaService.generateCaptcha(l1Request);
        assertNotNull(l1Response.getQuestion());
        
        // Test L2
        CaptchaGenerateRequest l2Request = new CaptchaGenerateRequest();
        l2Request.setDifficulty("L2");
        CaptchaGenerateResponse l2Response = captchaService.generateCaptcha(l2Request);
        assertNotNull(l2Response.getQuestion());
        
        // Test L3
        CaptchaGenerateRequest l3Request = new CaptchaGenerateRequest();
        l3Request.setDifficulty("L3");
        CaptchaGenerateResponse l3Response = captchaService.generateCaptcha(l3Request);
        assertNotNull(l3Response.getQuestion());
        assertTrue(l3Response.getQuestion().contains("find x"), "L3 should be algebraic");
    }
}
