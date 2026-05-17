package com.sporty.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;
import com.sporty.demo.service.EventOutcomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventOutcomeController.class)
class EventOutcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventOutcomeService eventOutcomeService;

    @Test
    void publishEventOutcome_returns202AndJsonBody() throws Exception {
        EventOutcomeRequest request = EventOutcomeRequest.builder()
                .eventId(1L)
                .eventName("Dinamo Zagreb vs HNK Hajduk Split")
                .eventWinnerId(1L)
                .build();
        EventOutcomeMessage response = EventOutcomeMessage.builder()
                .eventId(1L)
                .eventName("Dinamo Zagreb vs HNK Hajduk Split")
                .eventWinnerId(1L)
                .build();

        when(eventOutcomeService.publishEventOutcome(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/event-outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.eventName").value("Dinamo Zagreb vs HNK Hajduk Split"))
                .andExpect(jsonPath("$.eventWinnerId").value(1));

        verify(eventOutcomeService).publishEventOutcome(any(EventOutcomeRequest.class));
    }

    @Test
    void publishEventOutcome_invalidPayload_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/event-outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\":-1,\"eventName\":\"\",\"eventWinnerId\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publishEventOutcome_unsupportedMediaType_returns415() throws Exception {
        mockMvc.perform(post("/api/v1/event-outcomes")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("plain text"))
                .andExpect(status().isUnsupportedMediaType());
    }
}
