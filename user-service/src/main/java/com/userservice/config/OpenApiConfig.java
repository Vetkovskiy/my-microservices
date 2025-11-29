package com.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI/Swagger для генерации документации.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Настраивает объект OpenAPI с основной информацией о сервисе.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("1.0")
                        .description("Документация для UserController с HATEOAS")
                        .contact(new Contact()
                                .name("Vladimir")
                                .email("Fova@inbox.ru")));
    }
}
