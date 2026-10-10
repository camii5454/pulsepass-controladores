package com.pulsepass.dto.response;

import com.pulsepass.model.TicketStatus;
import com.pulsepass.model.TicketType;

import java.time.LocalDateTime;

public record TicketResponse(
        String ticketCode,
        String eventCode,
        String userEmail,
        TicketType type,
        TicketStatus status,
        LocalDateTime purchaseDate
) {
}