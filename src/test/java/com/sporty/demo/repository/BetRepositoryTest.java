package com.sporty.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sporty.demo.domain.Bet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BetRepositoryTest {

    @Autowired
    private BetRepository betRepository;

    @Test
    void findByEventId_returnsBetsForEvent() {
        assertThat(betRepository.findByEventId(1L)).hasSize(2);
    }

    @Test
    void findByEventId_returnsEmptyWhenNoBetsForEvent() {
        assertThat(betRepository.findByEventId(999L)).isEmpty();
    }

    @Test
    void findAll_loadsSeedData() {
        assertThat(betRepository.findAll()).hasSize(4).extracting(Bet::getBetId).containsExactly(1L, 2L, 3L, 4L);
    }
}
