package com.sporty.demo.support;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.domain.Event;
import com.sporty.demo.domain.Market;
import com.sporty.demo.domain.Team;
import com.sporty.demo.domain.User;
import java.math.BigDecimal;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Bet sampleBet() {
        return Bet.builder()
                .betId(1L)
                .userId(101L)
                .eventId(1L)
                .eventMarketId(1L)
                .eventWinnerId(1L)
                .user(User.builder().id(101L).name("Ivan").build())
                .event(Event.builder().id(1L).name("Dinamo Zagreb vs HNK Hajduk Split").build())
                .market(Market.builder().id(1L).name("Croatian League").build())
                .pickedWinner(Team.builder().id(1L).name("Dinamo Zagreb").build())
                .betAmount(new BigDecimal("50.00"))
                .build();
    }
}
