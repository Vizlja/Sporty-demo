package com.sporty.demo.kafka;

import com.sporty.demo.config.MessagingTopics;
import com.sporty.demo.dto.EventOutcomeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOutcomeKafkaProducer {

    private final KafkaTemplate<String, EventOutcomeMessage> kafkaTemplate;
    private final MessagingTopics messagingTopics;

    public void publish(EventOutcomeMessage message) {
        String topic = messagingTopics.getEventOutcomes();
        kafkaTemplate.send(topic, String.valueOf(message.getEventId()), message);
        log.info("Published event outcome to Kafka topic '{}': eventId={}", topic, message.getEventId());
    }
}
