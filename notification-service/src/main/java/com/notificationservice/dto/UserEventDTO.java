package com.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    /**
     * Тип события пользователя
     * Определяет, какое действие произошло с пользователем
     */
    public enum EventType {

        /**
         * Событие создания пользователя.
         * Генерируется при добавлении нового пользователя
         */
        CREATED,

        /**
         * Событие удаления пользователя.
         * Генерируется при удалении пользователя
         */
        DELETED
    }
}