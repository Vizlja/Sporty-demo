package com.sporty.demo.rocketmq;

import com.sporty.demo.config.MessagingTopics;
import com.sporty.demo.dto.BetSettlementMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetSettlementRocketProducer {

    private final RocketMQTemplate rocketMQTemplate;
    private final MessagingTopics messagingTopics;

    public void send(BetSettlementMessage settlement) {
        String topic = messagingTopics.getBetSettlements();
        rocketMQTemplate.syncSend(
                topic,
                MessageBuilder.withPayload(settlement).build());
        log.info(
                "Published bet settlement to RocketMQ topic '{}': betId={}, won={}, payout={}",
                topic,
                settlement.getBetId(),
                settlement.isWon(),
                settlement.getPayout());
    }
}
