package ru.kerporation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kerporation.event.OrderPlacedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(final OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        log.info("Received Notification for order: {}", orderPlacedEvent.getOrderNumber());
    }
}
