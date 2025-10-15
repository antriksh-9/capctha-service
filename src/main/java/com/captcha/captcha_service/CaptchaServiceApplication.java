package com.captcha.captcha_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CaptchaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaptchaServiceApplication.class, args);
	}

}
