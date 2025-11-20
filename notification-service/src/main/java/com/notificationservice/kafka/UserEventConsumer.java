package com.notificationservice.kafka;

import com.notificationservice.dto.UserEventDTO;
import com.notificationservice.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer для получения событий пользователей
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final EmailSender emailSender;

    @KafkaListener(
            topics = "${kafka.topic.user-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeUserEvent(UserEventDTO event) {
        log.info("Received user event from Kafka: {} for user: {}",
                event.getEventType(), event.getEmail());

        try {
            emailSender.sendUserEventEmail(event);
        } catch (Exception e) {
            log.error("Failed to process event: {}", event, e);
        }
    }
}

