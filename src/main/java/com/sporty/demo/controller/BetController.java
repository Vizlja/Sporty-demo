package com.sporty.demo.controller;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.repository.BetRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/bets",
                produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Bets", description = "Inspect bets stored in the in-memory database")
public class BetController {

    private final BetRepository betRepository;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "List all bets",
               description = "Returns all bets with related user, event, market, and picked winner.")
    public List<Bet> listBets() {
        return betRepository.findAll();
    }
}
