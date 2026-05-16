package com.sporty.demo.service;

import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;

public interface EventOutcomeService {

    EventOutcomeMessage publishEventOutcome(EventOutcomeRequest request);
}
