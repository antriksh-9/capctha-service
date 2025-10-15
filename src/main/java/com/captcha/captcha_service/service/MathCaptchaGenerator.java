package com.captcha.captcha_service.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.captcha.captcha_service.model.CaptchaData;

@Service
public class MathCaptchaGenerator {
    private final Random random = new Random();

    public CaptchaData generateCaptcha(String difficulty) {
        return switch (difficulty.toUpperCase()) {
            case "L1" -> generateL1();
            case "L2" -> generateL2();
            case "L3" -> generateL3();
            default -> generateL1();
        };
    }

    private CaptchaData generateL1() {
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        return generateOperation(a,b);
    }

    private CaptchaData generateL2() {
        int a = 10+random.nextInt(90);
        int b = 10+random.nextInt(90);
        return generateOperation(a,b);
    }

    private CaptchaData generateL3() {
        int x = 1+random.nextInt(10);
        int coefficient = 2+random.nextInt(5);
        int constant = random.nextInt(20);
        int result = coefficient * x + constant;

        String question = String.format("%dx + %d = %d, find x", coefficient, constant, result);
        return new CaptchaData(question,x,null);
    }

    private CaptchaData generateOperation(int a, int b) {
        int operation = random.nextInt(3);
        String question;
        int answer;

        switch(operation) {
            case 0:
                question = String.format("%d + %d", a, b);
                answer = a + b;
                break;
            case 1:
                if(a<b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                question = String.format("%d - %d", a, b);
                answer = a-b;
                break;
            case 2:
                question = String.format("%d + %d", a, b);
                answer = a*b;
                break;
            default:
                question = String.format("%d + %d", a, b);
                answer = a + b;    
        }
        return new CaptchaData(question, answer, null);   
    }
}
