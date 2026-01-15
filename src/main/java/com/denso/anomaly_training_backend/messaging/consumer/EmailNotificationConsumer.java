package com.denso.anomaly_training_backend.messaging.consumer;

import com.denso.anomaly_training_backend.dto.notification.EmailNotificationMessage;
import com.denso.anomaly_training_backend.messaging.config.RabbitMQConfig;
import com.denso.anomaly_training_backend.model.NotificationQueue;
import com.denso.anomaly_training_backend.model.NotificationTemplate;
import com.denso.anomaly_training_backend.repository.NotificationQueueRepository;
import com.denso.anomaly_training_backend.service.NotificationTemplateService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.rabbitmq", name = "enabled", havingValue = "true")
public class EmailNotificationConsumer {

    private final JavaMailSender mailSender;
    private final NotificationTemplateService templateService;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationQueueRepository notificationQueueRepository;

    private static final int MAX_RETRY_COUNT = 3;

    @RabbitListener(
            queues = RabbitMQConfig.NOTIFICATION_QUEUE,
            concurrency = "3-5" // Min 3, max 5 consumers
    )
    public void handleEmailNotification(EmailNotificationMessage message) {
        String correlationId = message.getCorrelationId();

        try {
            log.info("Processing email notification - CorrelationId: {}, Template: {}",
                    correlationId, message.getTemplateCode());

            // 1. Load template
            NotificationTemplate template = templateService.getTemplateByCode(message.getTemplateCode());

            // 2. Render template với variables
            String subject = templateService.renderSubject(
                    template.getSubjectTemplate(),
                    message.getVariables()
            );
            String body = templateService.renderBody(
                    template.getBodyTemplate(),
                    message.getVariables()
            );

            // 3. Gửi email
            sendEmail(message.getRecipientEmail(), subject, body);

            // 4. Lưu history
            saveHistory(message, "SENT", null);

            log.info("Email sent successfully - CorrelationId: {}", correlationId);

        } catch (MailException e) {
            log.error("Failed to send email - CorrelationId: {}", correlationId, e);
            handleFailure(message, e);

        } catch (Exception e) {
            log.error("Unexpected error processing email - CorrelationId: {}", correlationId, e);
            handleFailure(message, e);
        }
    }

    private void sendEmail(String to, String subject, String body) throws MailException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("noreply-training@denso.com", "DENSO Anomaly Training");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException | jakarta.mail.MessagingException e) {
            throw new MailException("Failed to create email", e) {};
        }
    }

    private void handleFailure(EmailNotificationMessage message, Exception error) {
        int retryCount = message.getRetryCount();

        if (retryCount < MAX_RETRY_COUNT) {
            // Retry: gửi vào retry queue
            message.setRetryCount(retryCount + 1);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.RETRY_EXCHANGE,
                    RabbitMQConfig.RETRY_ROUTING_KEY,
                    message
            );

            log.warn("Email queued for retry ({}/{}) - CorrelationId: {}",
                    retryCount + 1, MAX_RETRY_COUNT, message.getCorrelationId());

        } else {
            // Max retries reached, sẽ tự động vào Dead Letter Queue
            saveHistory(message, "FAILED", error.getMessage());

            log.error("Email permanently failed after {} retries - CorrelationId: {}",
                    MAX_RETRY_COUNT, message.getCorrelationId());
        }
    }

    // Consumer cho Dead Letter Queue để log lỗi
    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
    public void handleDeadLetter(EmailNotificationMessage message) {
        log.error("Email in Dead Letter Queue - CorrelationId: {}, Template: {}, Recipient: {}",
                message.getCorrelationId(),
                message.getTemplateCode(),
                message.getRecipientEmail());

        // Có thể gửi alert đến admin hoặc lưu vào DB đặc biệt để manual retry
        saveToFailedEmailsTable(message);
    }

    private void saveHistory(EmailNotificationMessage message, String status, String error) {
        NotificationQueue history = new NotificationQueue();
        history.setRecipientUserId(message.getRecipientUserId());
        history.setNotificationType(message.getTemplateCode());
        history.setStatus(status);
        history.setErrorMessage(error);
        history.setSentAt(Instant.from(LocalDateTime.now()));
        notificationQueueRepository.save(history);
    }

    private void saveToFailedEmailsTable(EmailNotificationMessage message) {
        // Implementation để admin có thể manual retry
    }
}