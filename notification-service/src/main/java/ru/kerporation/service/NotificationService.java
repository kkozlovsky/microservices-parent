package ru.kerporation.service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.kerporation.event.OrderPlacedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(final OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent);
            log.info("TraceId- {}, Received Notification for Order - {}",
                    this.tracer.currentSpan().context().traceId(),
                    orderPlacedEvent.getOrderNumber());
        });
        // send out an email notification
        log.info("Received Notification for order: {}", orderPlacedEvent.getOrderNumber());
    }
}
