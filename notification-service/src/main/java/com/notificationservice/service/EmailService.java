package com.notificationservice.service;

import com.notificationservice.dto.UserEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки email уведомлений
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.from-email}")
    private String fromEmail;

    /**
     * Отправка email на основе события пользователя
     */
    public void sendUserEventEmail(UserEventDTO event) {
        String subject;
        String message;

        if (event.getEventType() == UserEventDTO.EventType.CREATED) {
            subject = "Аккаунт успешно создан";
            message = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if (event.getEventType() == UserEventDTO.EventType.DELETED) {
            subject = "Аккаунт удалён";
            message = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            log.warn("Unknown event type: {}", event.getEventType());
            return;
        }

        sendEmail(event.getEmail(), subject, message);
    }

    /**
     * Отправка произвольного email
     */
    public void sendEmail(String to, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

