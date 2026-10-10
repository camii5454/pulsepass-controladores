package com.pulsepass.dto.response;

import com.pulsepass.model.EventCategory;
import com.pulsepass.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        String eventCode,
        String name,
        String description,
        EventCategory category,
        EventStatus status,
        LocalDateTime eventDate,
        Integer minimumAge,
        VenueResponse venue,
        List<ArtistResponse> artists
) {
}