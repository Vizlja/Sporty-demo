package com.sporty.demo.dto;

import java.math.BigDecimal;
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
public class BetSettlementMessage {

    private Long betId;
    private Long userId;
    private Long eventId;
    private String eventName;
    private Long eventMarketId;
    private Long eventWinnerId;
    private Long actualWinnerId;
    private BigDecimal betAmount;
    private boolean won;
    private BigDecimal payout;
}
