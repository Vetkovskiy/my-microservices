package com.userservice.controller;

import com.userservice.dto.UserCreateDTO;
import com.userservice.dto.UserResponseDTO;
import com.userservice.dto.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Спецификация REST API для управления пользователями.
 * Содержит операции CRUD и проверку email.
 * Документируется через Swagger/OpenAPI.
 */
@Tag(name = "Users", description = "Операции с пользователями")
@RequestMapping("/api/v1/users")
public interface UserControllerApi {

    /**
     * Создать нового пользователя
     * POST /api/v1/users
     */
    @Operation(summary = "Создать нового пользователя")
    @PostMapping
    ResponseEntity<EntityModel<UserResponseDTO>> createUser(
            @Valid @RequestBody UserCreateDTO request);

    /**
     * Получить пользователя по ID
     * GET /api/v1/users/{id}
     */
    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    ResponseEntity<EntityModel<UserResponseDTO>> getUserById(
            @PathVariable Long id);

    /**
     * Получить всех пользователей
     * GET /api/v1/users/
     */
    @Operation(summary = "Получить список всех пользователей")
    @GetMapping
    ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> getAllUsers();

    /**
     * Обновить пользователя
     * PUT /api/v1/users/{id}
     */
    @Operation(summary = "Обновить пользователя по ID")
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<UserResponseDTO>> updateUser(@PathVariable Long id,
                                                            @Valid @RequestBody UserUpdateDTO request);

    /**
     * Удалить пользователя
     * DELETE /api/v1/users/{id}
     */
    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя с указанным идентификатором. Возвращает статус 204 No Content при успешном удалении."
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);

    /**
     * Проверить существование email
     * GET /api/v1/users/exists?email=test@example.com
     */
    @Operation(summary = "Проверить, существует ли email")
    @GetMapping("/exists")
    ResponseEntity<Boolean> existsByEmail(@RequestParam String email);
}
