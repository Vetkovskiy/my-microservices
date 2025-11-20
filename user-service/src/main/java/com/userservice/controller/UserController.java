package com.userservice.controller;

import com.userservice.dto.UserCreateDTO;
import com.userservice.dto.UserResponseDTO;
import com.userservice.dto.UserUpdateDTO;
import com.userservice.hateoas.UserModelAssembler;
import com.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    /**
     * Создать нового пользователя
     * POST /api/v1/users
     */
    @Operation(summary = "Создать нового пользователя")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponseDTO>> createUser(
            @Valid @RequestBody UserCreateDTO request) {

        log.debug("REST request to create user: {}", request.getEmail());
        UserResponseDTO created = userService.createUser(request);
        EntityModel<UserResponseDTO> model = userModelAssembler.toModel(created);

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserById(created.getId())).toUri())
                .body(model);
    }

    /**
     * Получить пользователя по ID
     * GET /api/v1/users/{id}
     */
    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> getUserById(
            @PathVariable Long id) {

        log.debug("REST request to get user by id: {}", id);
        UserResponseDTO user = userService.getUserById(id);

        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    /**
     * Получить всех пользователей
     * GET /api/v1/users/
     */
    @Operation(summary = "Получить список всех пользователей")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> getAllUsers() {

        log.debug("REST request to get all users");
        List<EntityModel<UserResponseDTO>> users = userService.getAllUsers().stream()
                .map(userModelAssembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));

    }

    /**
     * Обновить пользователя
     * PUT /api/v1/users/{id}
     */
    @Operation(summary = "Обновить пользователя по ID")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO request) {

        log.debug("REST request to update user with id: {}", id);
        UserResponseDTO updated = userService.updateUser(id, request);
        EntityModel<UserResponseDTO> model = userModelAssembler.toModel(updated);

        return ResponseEntity.ok(model);
    }

    /**
     * Удалить пользователя
     * DELETE /api/v1/users/{id}
     */
    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя с указанным идентификатором. Возвращает статус 204 No Content при успешном удалении."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        log.debug("REST request to delete user with id: {}", id);
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Проверить существование email
     * GET /api/v1/users/exists?email=test@example.com
     */
    @Operation(summary = "Проверить, существует ли email")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(
            @RequestParam String email) {

        log.debug("REST request to check if email exists: {}", email);
        boolean exists = userService.existsByEmail(email);

        return ResponseEntity.ok(exists);
    }
}
