package com.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для ответа API с данными пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Schema(description = "DTO для ответа с данными пользователя")
public class UserResponseDTO {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(description = "ID пользователя", example = "15")
    private Long id;

    /**
     * Имя пользователя.
     */
    @Schema(description = "Имя пользователя", example = "Ivan")
    private String name;

    /**
     * Электронная почта пользователя.
     */
    @Schema(description = "Электронная почта пользователя", example = "ivan.petrov@example.com")
    private String email;

    /**
     * Возраст пользователя.
     */
    @Schema(description = "Возраст пользователя", example = "30")
    private Integer age;

    /**
     * Дата и время создания пользователя.
     * <p>Форматируется в JSON как {@code yyyy-MM-dd'T'HH:mm:ss}.</p>
     */
    @Schema(description = "Дата и время создания пользователя", example = "2025-11-20T20:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
