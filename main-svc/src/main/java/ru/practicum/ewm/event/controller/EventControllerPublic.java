package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.GetEventsPublicRequest;
import ru.practicum.ewm.event.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request
    ) {
        GetEventsPublicRequest getEventsPublicRequest = new GetEventsPublicRequest(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request.getRequestURI(), request.getRemoteAddr());
        return eventService.getEvents(getEventsPublicRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable @Min(1) Long eventId, HttpServletRequest request) {
        return eventService.getEventById(eventId, request.getRequestURI(), request.getRemoteAddr());
    }
}
