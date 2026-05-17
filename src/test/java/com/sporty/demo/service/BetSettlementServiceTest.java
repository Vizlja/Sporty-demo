package com.sporty.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.dto.BetSettlementMessage;
import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.repository.BetRepository;
import com.sporty.demo.rocketmq.BetSettlementRocketProducer;
import com.sporty.demo.service.impl.BetSettlementServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetSettlementServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetSettlementRocketProducer betSettlementRocketProducer;

    @InjectMocks
    private BetSettlementServiceImpl betSettlementService;

    @Test
    void settleBetsForEventOutcome_publishesSettlementForEachMatchingBet() {
        Bet winningBet = Bet.builder()
                .betId(1L)
                .userId(101L)
                .eventId(1L)
                .eventMarketId(1L)
                .eventWinnerId(1L)
                .betAmount(new BigDecimal("50.00"))
                .build();

        when(betRepository.findByEventId(1L)).thenReturn(List.of(winningBet));

        EventOutcomeMessage outcome = EventOutcomeMessage.builder()
                .eventId(1L)
                .eventName("Final")
                .eventWinnerId(1L)
                .build();

        ArgumentCaptor<BetSettlementMessage> captor = ArgumentCaptor.forClass(BetSettlementMessage.class);

        betSettlementService.settleBetsForEventOutcome(outcome);

        verify(betSettlementRocketProducer).send(captor.capture());
        BetSettlementMessage settlement = captor.getValue();
        assertThat(settlement.isWon()).isTrue();
        assertThat(settlement.getPayout()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(settlement.getEventWinnerId()).isEqualTo(1L);
        assertThat(settlement.getActualWinnerId()).isEqualTo(1L);
    }

    @Test
    void settleBetsForEventOutcome_marksLostBetWithZeroPayout() {
        Bet losingBet = Bet.builder()
                .betId(2L)
                .userId(102L)
                .eventId(1L)
                .eventMarketId(1L)
                .eventWinnerId(3L)
                .betAmount(new BigDecimal("25.00"))
                .build();

        when(betRepository.findByEventId(1L)).thenReturn(List.of(losingBet));

        EventOutcomeMessage outcome = EventOutcomeMessage.builder()
                .eventId(1L)
                .eventName("Dinamo Zagreb vs HNK Hajduk Split")
                .eventWinnerId(1L)
                .build();

        ArgumentCaptor<BetSettlementMessage> captor = ArgumentCaptor.forClass(BetSettlementMessage.class);

        betSettlementService.settleBetsForEventOutcome(outcome);

        verify(betSettlementRocketProducer).send(captor.capture());
        assertThat(captor.getValue().isWon()).isFalse();
        assertThat(captor.getValue().getPayout()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void settleBetsForEventOutcome_doesNothingWhenNoBetsFound() {
        when(betRepository.findByEventId(999L)).thenReturn(List.of());

        EventOutcomeMessage outcome = EventOutcomeMessage.builder()
                .eventId(999L)
                .eventName("Unknown")
                .eventWinnerId(1L)
                .build();

        betSettlementService.settleBetsForEventOutcome(outcome);

        verify(betSettlementRocketProducer, never()).send(any());
    }
}
