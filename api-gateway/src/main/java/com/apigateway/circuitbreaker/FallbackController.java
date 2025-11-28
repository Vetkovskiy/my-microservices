package com.apigateway.circuitbreaker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fallback контроллер для user-service.
 * Возвращает статус 503 и сообщение при недоступности сервиса.
 */
@RestController
public class FallbackController {

    @RequestMapping("/fallback/users")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User-service временно недоступен, попробуйте позже.");
    }
}
