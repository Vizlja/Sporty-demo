package com.sporty.demo.controller;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.repository.BetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetRepository betRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public List<Bet> listBets() {
        return betRepository.findAll();
    }
}
