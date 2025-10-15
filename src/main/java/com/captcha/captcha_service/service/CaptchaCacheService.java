package com.captcha.captcha_service.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.captcha.captcha_service.model.CaptchaData;


@Service
public class CaptchaCacheService {
    
    private final RedisTemplate<String, CaptchaData> redisTemplate;

    @Value("${captcha.ttl:180}")
    private long ttl;

    public CaptchaCacheService(RedisTemplate<String, CaptchaData> redisTemplate) {
        this.redisTemplate = redisTemplate;
    } 

    public void store(String captchaId, CaptchaData data) {
        redisTemplate.opsForValue().set(captchaId, data, ttl, TimeUnit.SECONDS);
    }

    public CaptchaData retrieve(String captchaId) {
        return redisTemplate.opsForValue().get(captchaId);
    }

    public void delete(String captchaId) {
        redisTemplate.delete(captchaId);
    }

    public boolean exists(String captchaId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(captchaId));
    }
}
