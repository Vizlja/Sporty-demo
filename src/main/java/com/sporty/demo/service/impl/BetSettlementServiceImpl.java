package com.sporty.demo.service.impl;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.dto.BetSettlementMessage;
import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.repository.BetRepository;
import com.sporty.demo.rocketmq.BetSettlementRocketProducer;
import com.sporty.demo.service.BetSettlementService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetSettlementServiceImpl implements BetSettlementService {

    private final BetRepository betRepository;
    private final BetSettlementRocketProducer betSettlementRocketProducer;

    @Override
    @Transactional(readOnly = true)
    public void settleBetsForEventOutcome(EventOutcomeMessage outcome) {
        List<Bet> bets = betRepository.findByEventId(outcome.getEventId());

        if (bets.isEmpty()) {
            log.info("No bets found for eventId={}", outcome.getEventId());
            return;
        }

        log.info("Found {} bet(s) to settle for eventId={}", bets.size(), outcome.getEventId());

        for (Bet bet : bets) {
            BetSettlementMessage settlement = buildSettlement(bet, outcome);
            betSettlementRocketProducer.send(settlement);
        }
    }

    private BetSettlementMessage buildSettlement(Bet bet, EventOutcomeMessage outcome) {
        boolean won = bet.getEventWinnerId().equals(outcome.getEventWinnerId());
        BigDecimal payout = won ? bet.getBetAmount().multiply(BigDecimal.valueOf(2)) : BigDecimal.ZERO;

        return BetSettlementMessage.builder()
                .betId(bet.getBetId())
                .userId(bet.getUserId())
                .eventId(bet.getEventId())
                .eventName(outcome.getEventName())
                .eventMarketId(bet.getEventMarketId())
                .eventWinnerId(bet.getEventWinnerId())
                .actualWinnerId(outcome.getEventWinnerId())
                .betAmount(bet.getBetAmount())
                .won(won)
                .payout(payout)
                .build();
    }
}
