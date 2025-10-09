package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GetEventsAdminRequest(
        List<Long> users,
        List<String> states,
        List<Long> categories,
        LocalDateTime rangeStart,
        LocalDateTime rangeEnd,
        Integer from,
        Integer size
) {
}
