package com.sporty.demo.controller;

import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;
import com.sporty.demo.service.EventOutcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-outcomes")
@RequiredArgsConstructor
public class EventOutcomeController {

    private final EventOutcomeService eventOutcomeService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EventOutcomeMessage publishEventOutcome(@Valid @RequestBody EventOutcomeRequest request) {
        return eventOutcomeService.publishEventOutcome(request);
    }
}
