package com.userservice.controller;

import com.userservice.dto.UserCreateDTO;
import com.userservice.dto.UserResponseDTO;
import com.userservice.dto.UserUpdateDTO;
import com.userservice.hateoas.UserModelAssembler;
import com.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Реализация спецификации UserControllerApi.
 * Использует HATEOAS для добавления ссылок в ответы.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserControllerApi {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public ResponseEntity<EntityModel<UserResponseDTO>> createUser(
            @Valid @RequestBody UserCreateDTO request) {

        log.debug("REST request to create user: {}", request.getEmail());
        UserResponseDTO created = userService.createUser(request);
        EntityModel<UserResponseDTO> model = userModelAssembler.toModel(created);

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserById(created.getId())).toUri())
                .body(model);
    }

    public ResponseEntity<EntityModel<UserResponseDTO>> getUserById(
            @PathVariable Long id) {

        log.debug("REST request to get user by id: {}", id);
        UserResponseDTO user = userService.getUserById(id);

        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    public ResponseEntity<CollectionModel<EntityModel<UserResponseDTO>>> getAllUsers() {

        log.debug("REST request to get all users");
        List<EntityModel<UserResponseDTO>> users = userService.getAllUsers().stream()
                .map(userModelAssembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));

    }

    public ResponseEntity<EntityModel<UserResponseDTO>> updateUser(@PathVariable Long id,
                                                                   @Valid @RequestBody UserUpdateDTO request) {

        log.debug("REST request to update user with id: {}", id);
        UserResponseDTO updated = userService.updateUser(id, request);
        EntityModel<UserResponseDTO> model = userModelAssembler.toModel(updated);

        return ResponseEntity.ok(model);
    }

    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        log.debug("REST request to delete user with id: {}", id);
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Boolean> existsByEmail(
            @RequestParam String email) {

        log.debug("REST request to check if email exists: {}", email);
        boolean exists = userService.existsByEmail(email);

        return ResponseEntity.ok(exists);
    }
}
