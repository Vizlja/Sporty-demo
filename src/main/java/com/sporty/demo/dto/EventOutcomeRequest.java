package com.sporty.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOutcomeRequest {

    @NotNull
    @Positive
    private Long eventId;

    @NotBlank
    private String eventName;

    @NotNull
    @Positive
    private Long eventWinnerId;
}
