package com.captcha.captcha_service.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.captcha.captcha_service.model.CaptchaValidateRequest;
import com.captcha.captcha_service.model.*;
import com.captcha.captcha_service.service.CaptchaService;

import java.time.Duration;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<CaptchaGenerateResponse> generateCaptcha(
            @RequestBody(required = false) CaptchaGenerateRequest request, HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        Bucket bucket = resolveBucket(clientIp);

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        if (request == null) {
            request = new CaptchaGenerateRequest();
        }

        CaptchaGenerateResponse response = captchaService.generateCaptcha(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<CaptchaValidateResponse> validateCaptcha(@RequestBody CaptchaValidateRequest request,
            HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        Bucket bucket = resolveBucket(clientIp);

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        CaptchaValidateResponse response = captchaService.validateCaptcha(request);
        return ResponseEntity.ok(response);
    }

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> createNewBucket());
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(60)
                .refillIntervally(60, Duration.ofMinutes(1))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarede-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
