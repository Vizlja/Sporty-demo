package com.sporty.demo.service.impl;

import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;
import com.sporty.demo.kafka.EventOutcomeKafkaProducer;
import com.sporty.demo.service.EventOutcomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventOutcomeServiceImpl implements EventOutcomeService {

    private final EventOutcomeKafkaProducer eventOutcomeKafkaProducer;

    @Override
    public EventOutcomeMessage publishEventOutcome(EventOutcomeRequest request) {
        EventOutcomeMessage message = EventOutcomeMessage.builder()
                .eventId(request.getEventId())
                .eventName(request.getEventName())
                .eventWinnerId(request.getEventWinnerId())
                .build();
        eventOutcomeKafkaProducer.publish(message);
        return message;
    }
}
