package com.pulsepass.dto.response;

import com.pulsepass.model.EventCategory;
import com.pulsepass.model.EventStatus;

import java.time.LocalDateTime;

public record EventSummaryResponse(
        String eventCode,
        String name,
        EventCategory category,
        EventStatus status,
        LocalDateTime eventDate
) {
}