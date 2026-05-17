package com.sporty.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Sports event identifier", example = "1")
    private Long eventId;

    @NotBlank
    @Schema(description = "Human-readable event name", example = "Dinamo Zagreb vs HNK Hajduk Split")
    private String eventName;

    @NotNull
    @Positive
    @Schema(description = "Actual winning team identifier", example = "1")
    private Long eventWinnerId;
}
