package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public record GetEventsPublicRequest(
        String text,
        List<Long> categories,
        Boolean paid,
        LocalDateTime rangeStart,
        LocalDateTime rangeEnd,
        Boolean onlyAvailable,
        EventSort sort,
        Integer from,
        Integer size,
        String uri,
        String ip
) {
}
