package com.denso.anomaly_training_backend.messaging.producer;

import com.denso.anomaly_training_backend.dto.notification.EmailNotificationMessage;
import com.denso.anomaly_training_backend.messaging.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.rabbitmq", name = "enabled", havingValue = "true")
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailNotification(EmailNotificationMessage message) {
        try {
            // Tạo correlation ID để trace
            message.setCorrelationId(UUID.randomUUID().toString());

            // Gửi vào queue với priority
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                    message,
                    msg -> {
                        msg.getMessageProperties().setPriority(message.getPriority());
                        msg.getMessageProperties().setContentType("application/json");
                        msg.getMessageProperties().setHeader("x-retry-count", message.getRetryCount());
                        return msg;
                    }
            );

            log.info("Email notification queued - Template: {}, Recipient: {}, CorrelationId: {}",
                    message.getTemplateCode(),
                    message.getRecipientEmail(),
                    message.getCorrelationId());

        } catch (Exception e) {
            log.error("Failed to queue email notification", e);
            // Có thể lưu vào DB backup nếu RabbitMQ down
            saveToBackupQueue(message);
        }
    }

    // Batch send cho việc gửi hàng loạt reminder
    public void sendBatchNotifications(List<EmailNotificationMessage> messages) {
        messages.forEach(this::sendEmailNotification);
    }

    private void saveToBackupQueue(EmailNotificationMessage message) {
        // Fallback: lưu vào DB nếu RabbitMQ không available
        // Sẽ có scheduled job để retry gửi lại
    }
}
