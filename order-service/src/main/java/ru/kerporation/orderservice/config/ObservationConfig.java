package ru.kerporation.orderservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ObservationConfig {

    private final KafkaTemplate kafkaTemplate;

    @PostConstruct
    void setup() {
        this.kafkaTemplate.setObservationEnabled(true);
    }

}
