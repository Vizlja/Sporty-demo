package com.sporty.demo.kafka;

import static org.mockito.Mockito.verify;

import com.sporty.demo.config.MessagingTopics;
import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.service.BetSettlementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventOutcomeKafkaConsumerTest {

    @Mock
    private BetSettlementService betSettlementService;

    private EventOutcomeKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        MessagingTopics messagingTopics = new MessagingTopics(new com.sporty.demo.config.AppProperties());
        consumer = new EventOutcomeKafkaConsumer(betSettlementService, messagingTopics);
    }

    @Test
    void onEventOutcome_delegatesToSettlementService() {
        EventOutcomeMessage message = EventOutcomeMessage.builder()
                .eventId(1L)
                .eventName("Dinamo Zagreb vs HNK Hajduk Split")
                .eventWinnerId(1L)
                .build();

        consumer.onEventOutcome(message);

        verify(betSettlementService).settleBetsForEventOutcome(message);
    }
}
