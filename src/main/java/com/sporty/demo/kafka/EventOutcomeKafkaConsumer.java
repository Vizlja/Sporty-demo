package com.sporty.demo.kafka;

import com.sporty.demo.config.MessagingTopics;
import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.service.BetSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOutcomeKafkaConsumer {

    private final BetSettlementService betSettlementService;
    private final MessagingTopics messagingTopics;

    @KafkaListener(
            topics = "${app.kafka.topic.event-outcomes}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "eventOutcomeKafkaListenerContainerFactory")
    public void onEventOutcome(EventOutcomeMessage message) {
        log.info(
                "Received event outcome from Kafka topic '{}': eventId={}, winner={}",
                messagingTopics.getEventOutcomes(),
                message.getEventId(),
                message.getEventWinnerId());
        betSettlementService.settleBetsForEventOutcome(message);
    }
}
