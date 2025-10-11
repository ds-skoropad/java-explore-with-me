package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsForUser(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return eventService.getEventsForUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventForUser(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST createEventForUser: userId={}, {}", userId, newEventDto);
        return eventService.createEventForUser(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId) {
        return eventService.getEventByIdForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByIdForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @RequestBody @Valid UpdateEventUserRequest request) {
        log.info("PATCH updateEventByIdForUser: userId= {}, eventId={}, {}", userId, eventId, request);
        return eventService.updateEventByIdForUser(userId, eventId, request);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequestsForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId) {
        return eventService.getEventRequestsForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        log.info("PATCH updateEventRequestsForUser: userId= {}, eventId={}, {}", userId, eventId, request);
        return eventService.updateEventRequestsForUser(userId, eventId, request);
    }
}
