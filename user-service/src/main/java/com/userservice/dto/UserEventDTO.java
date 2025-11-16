package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO для событий пользователя (отправка в Kafka)
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserEventDTO {

    private EventType eventType;
    private String email;

    public enum EventType {
        CREATED,
        DELETED
    }
}

