# Mathematical CAPTCHA Microservice
A production-ready Spring Boot microservice that generates and validates arithmetic CAPTCHA challenges with configurable difficulty levels, Redis caching, rate limiting, and support for both text and image-based formats
## 🎯 Overview
This microservice provides a reusable CAPTCHA solution that generates mathematical challenges (addition, subtraction, multiplication) with three difficulty levels. Each CAPTCHA has a unique token with a short TTL (2-3 minutes) and can be rendered as either plain text or a Base64-encoded PNG image.

Author: Pawan Sharma
Target Audience: Java Developers
Stack: Spring Boot 3.x, Java 17/21, Redis
## ✨ Features
### ✅ Three Difficulty Levels:

- L1: Single-digit arithmetic (0-9)

- L2: Two-digit arithmetic (10-99)

- L3: Simple algebraic expressions (e.g., "2x + 4 = 10, find x")

### ✅ Dual Format Support:

- Text-based questions

- Base64 PNG image generation

### ✅ Security Features:

- Unique CAPTCHA IDs with 3-minute TTL

- One-time token usage

- IP-based rate limiting (60 requests/minute)

- Redis-backed distributed caching

### ✅ Production Ready:

- Health check endpoints

- Horizontal scaling support

- Docker containerization

- Comprehensive error handling

## 🛠️ Tech Stack
- Framework -> Spring Boot->	3.2.0+
- Language ->	Java ->	17 or 21
- Cache	-> Redis ->	7.x
- Build Tool	-> Maven ->	3.8+
- Rate Limiting ->	Bucket4j -> 8.15.0

## 🚀 Installation
### Step 1: Clone the Repository
`git clone https://github.com/YOUR_USERNAME/captcha-service.git
cd captcha-service
`
### Step 2: Start Redis
`redis-server
`
Verify Redis is running:
`redis-cli ping`
### Step 3: Build the Project
`mvn clean install
`
This will:

- Download all dependencies

- Compile the source code

- Run all tests

- Create a JAR file in target/ directory

### Step 4: Run the Application
`mvn spring-boot:run
`

## 📡 API Documentation

### Documentation URL: https://documenter.getpostman.com/view/29968557/2sB3QQJ829
### Base URL
`http://localhost:5000
`
### 1. Generate CAPTCHA
Endpoint: `POST /captcha/generate`

Description: Generates a new CAPTCHA challenge

- Request Body:
`{
  "difficulty": "L1",    // L1, L2, or L3 (optional, default: L1)
  "asImage": false       // true for image, false for text (optional, default: false)
}
`
- Response (Text-based):
`{
  "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "question": "7 + 3",
  "imageBase64": null,
  "expiresIn": 180
}
`
- Response (Image-based):
`{
  "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "question": "7 + 3",
  "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
  "expiresIn": 180
}
`
### 2. Validate CAPTCHA
Endpoint: `POST /captcha/validate`

Description: Validates the user's answer to a CAPTCHA

- Request Body:

`{
  "captchaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "answer": 10
}
`
- Response (Success):
`{
  "success": true,
  "message": "CAPTCHA validation successful"
}
`
- Response (Failure):
`{
  "success": false,
  "message": "Incorrect answer"
}
`
## 🧪 Testing
### Run All Tests
`mvn test
`
