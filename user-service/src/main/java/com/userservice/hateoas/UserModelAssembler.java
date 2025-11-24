package com.userservice.hateoas;

import com.userservice.controller.UserController;
import com.userservice.dto.UserResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Ассемблер модели пользователя для HATEOAS.
 */
@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseDTO, EntityModel<UserResponseDTO>> {

    /**
     * Преобразует DTO пользователя в HATEOAS-модель.
     */
    @Override
    public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
                linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"),
                linkTo(methodOn(UserController.class).existsByEmail(user.getEmail())).withRel("check-email")
        );
    }
}
