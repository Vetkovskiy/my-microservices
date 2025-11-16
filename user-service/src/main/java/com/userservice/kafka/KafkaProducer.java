package com.userservice.kafka;


import com.userservice.dto.UserEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer для отправки событий пользователей в Kafka
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, UserEventDTO> kafkaTemplateNotification;

    @Value("${kafka.topic.user-events}")
    private String topic;

    /**
     * Публикация события в Kafka
     */
    public void sendUserEvent(UserEventDTO event) {
        log.debug("Sending event to Kafka: email={}, type={}", event.getEmail(), event.getEventType());

        kafkaTemplateNotification.send(topic, event.getEmail(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event to Kafka: {}", event, ex);
                    } else {
                        log.info("Event sent successfully: {}", event.getEventType());
                    }
                });
    }
}
