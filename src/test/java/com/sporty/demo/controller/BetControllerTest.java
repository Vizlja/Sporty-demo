package com.sporty.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sporty.demo.domain.Bet;
import com.sporty.demo.repository.BetRepository;
import com.sporty.demo.support.TestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BetController.class)
class BetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BetRepository betRepository;

    @Test
    void listBets_returnsJsonWithoutDuplicateForeignKeyIds() throws Exception {
        Bet bet = TestDataFactory.sampleBet();
        when(betRepository.findAll()).thenReturn(List.of(bet));

        mockMvc.perform(get("/api/v1/bets").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].betId").value(1))
                .andExpect(jsonPath("$[0].user.id").value(101))
                .andExpect(jsonPath("$[0].user.name").value("Ivan"))
                .andExpect(jsonPath("$[0].event.id").value(1))
                .andExpect(jsonPath("$[0].market.id").value(1))
                .andExpect(jsonPath("$[0].pickedWinner.id").value(1))
                .andExpect(jsonPath("$[0].userId").doesNotExist())
                .andExpect(jsonPath("$[0].eventId").doesNotExist())
                .andExpect(jsonPath("$[0].eventMarketId").doesNotExist())
                .andExpect(jsonPath("$[0].eventWinnerId").doesNotExist());
    }
}
