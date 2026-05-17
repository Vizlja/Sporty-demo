package com.sporty.demo.controller;

import com.sporty.demo.dto.EventOutcomeMessage;
import com.sporty.demo.dto.EventOutcomeRequest;
import com.sporty.demo.service.EventOutcomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Event outcomes", description = "Publish sports event outcomes to Kafka")
public class EventOutcomeController {

    private final EventOutcomeService eventOutcomeService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Publish event outcome",
               description = "Publishes an event outcome to the Kafka topic `event-outcomes` for bet settlement.")
    @ApiResponse(responseCode = "202", description = "Outcome accepted and sent to Kafka")
    public EventOutcomeMessage publishEventOutcome(@Valid @RequestBody EventOutcomeRequest request) {
        return eventOutcomeService.publishEventOutcome(request);
    }
}
