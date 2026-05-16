package com.sporty.demo.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MessagingTopics {

    private final String eventOutcomes;
    private final String betSettlements;

    public MessagingTopics(AppProperties appProperties) {
        this.eventOutcomes = appProperties.getKafka().getTopic().getEventOutcomes();
        this.betSettlements = appProperties.getRocketmq().getTopic().getBetSettlements();
    }
}
