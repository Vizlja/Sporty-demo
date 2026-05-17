package com.sporty.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;

import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;
import com.sporty.demo.kafka.EventOutcomeKafkaProducer;
import com.sporty.demo.service.impl.EventOutcomeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventOutcomeServiceImplTest {

    @Mock
    private EventOutcomeKafkaProducer eventOutcomeKafkaProducer;

    @InjectMocks
    private EventOutcomeServiceImpl eventOutcomeService;

    @Test
    void publishEventOutcome_buildsMessageAndPublishesToKafka() {
        EventOutcomeRequest request = EventOutcomeRequest.builder()
                .eventId(1L)
                .eventName("Dinamo Zagreb vs HNK Hajduk Split")
                .eventWinnerId(1L)
                .build();

        EventOutcomeMessage result = eventOutcomeService.publishEventOutcome(request);

        assertThat(result.getEventId()).isEqualTo(1L);
        assertThat(result.getEventName()).isEqualTo("Dinamo Zagreb vs HNK Hajduk Split");
        assertThat(result.getEventWinnerId()).isEqualTo(1L);

        verify(eventOutcomeKafkaProducer).publish(assertArg(message -> {
            assertThat(message.getEventId()).isEqualTo(1L);
            assertThat(message.getEventWinnerId()).isEqualTo(1L);
        }));
    }
}
