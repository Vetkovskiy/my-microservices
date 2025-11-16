package com.notificationservice.kafka;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.notificationservice.dto.UserEventDTO;
import jakarta.mail.internet.MimeMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Интеграционный тест для Kafka consumer с отправкой email
 */
@SpringBootTest
@Testcontainers
class UserEventConsumerIntegrationTest {

    @Container
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"))
            .withReuse(true);

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"))
            .withPerMethodLifecycle(false);

    private static final String TOPIC = "user.events";

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("kafka.topic.user-events", () -> TOPIC);

        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
        registry.add("spring.mail.username", () -> "test");
        registry.add("spring.mail.password", () -> "test");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.required", () -> false);
        registry.add("spring.mail.properties.mail.transport.protocol", () -> "smtp");
        registry.add("notification.from-email", () -> "noreply@test.com");
    }

    @Test
    void shouldSendEmailWhenUserCreatedEventReceived() {
        UserEventDTO event = UserEventDTO.builder()
                .email("newuser@example.com")
                .eventType(UserEventDTO.EventType.CREATED)
                .build();

        KafkaTemplate<String, UserEventDTO> kafkaTemplate = createKafkaTemplate();

        kafkaTemplate.send(TOPIC, event.getEmail(), event);

        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
                    assertThat(receivedMessages).hasSize(1);

                    MimeMessage message = receivedMessages[0];
                    assertThat(message.getSubject()).isEqualTo("Аккаунт успешно создан");
                    assertThat(message.getContent().toString())
                            .contains("Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
                    assertThat(message.getAllRecipients()[0].toString()).isEqualTo("newuser@example.com");
                });
    }

    @Test
    void shouldSendEmailWhenUserDeletedEventReceived() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();

        UserEventDTO event = UserEventDTO.builder()
                .email("deleteduser@example.com")
                .eventType(UserEventDTO.EventType.DELETED)
                .build();

        KafkaTemplate<String, UserEventDTO> kafkaTemplate = createKafkaTemplate();

        kafkaTemplate.send(TOPIC, event.getEmail(), event);

        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
                    assertThat(receivedMessages).hasSize(1);

                    MimeMessage message = receivedMessages[0];
                    assertThat(message.getSubject()).isEqualTo("Аккаунт удалён");
                    assertThat(message.getContent().toString())
                            .contains("Здравствуйте! Ваш аккаунт был удалён.");
                    assertThat(message.getAllRecipients()[0].toString()).isEqualTo("deleteduser@example.com");
                });
    }

    private KafkaTemplate<String, UserEventDTO> createKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        ProducerFactory<String, UserEventDTO> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}
