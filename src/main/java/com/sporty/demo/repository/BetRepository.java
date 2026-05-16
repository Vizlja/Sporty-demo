package com.sporty.demo.repository;

import com.sporty.demo.domain.Bet;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {

    List<Bet> findByEventId(Long eventId);

    @Override
    @EntityGraph(attributePaths = {"user", "event", "market", "pickedWinner"})
    List<Bet> findAll();
}
