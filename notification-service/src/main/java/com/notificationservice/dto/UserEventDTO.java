package com.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO события пользователя (для десериализации из Kafka)
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