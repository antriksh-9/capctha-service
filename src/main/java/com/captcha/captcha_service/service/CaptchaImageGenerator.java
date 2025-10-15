package com.captcha.captcha_service.service;

import java.util.Random;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CaptchaImageGenerator {
    private final Random random = new Random();
    
    public String generateImage(String text) {
        try {
            int width = 300;
            int height = 100;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,width,height);

            g2d.setColor(Color.LIGHT_GRAY);
            for(int i=0; i<5; i++) {
                g2d.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.setColor(Color.BLACK);

            FontMetrics fm = g2d.getFontMetrics();
            int x = (width - fm.stringWidth(text))/2;
            int y = ((height-fm.getHeight())/2)+fm.getAscent();

            g2d.drawString(text,x,y);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image,"png",baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch(Exception e) {
            throw new RuntimeException("Failed to genrate CAPTCHA image",e);
        }
    }
}
