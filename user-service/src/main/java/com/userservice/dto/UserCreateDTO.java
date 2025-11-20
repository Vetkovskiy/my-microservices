package com.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO для создания пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Schema(description = "DTO для создания нового пользователя")
public class UserCreateDTO {
    @Schema(description = "Имя пользователя", example = "Vladimir")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Schema(description = "Email пользователя", example = "newuser@example.com")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @Schema(description = "Возраст пользователя", example = "35")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must be at most 150")
    private Integer age;
}
