package com.sporty.demo.service;

import com.sporty.demo.dto.EventOutcomeMessage;

public interface BetSettlementService {

    void settleBetsForEventOutcome(EventOutcomeMessage outcome);
}
