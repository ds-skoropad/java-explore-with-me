package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.GetEventsAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        GetEventsAdminRequest getEventsAdminRequest = new GetEventsAdminRequest(users, states, categories, rangeStart,
                rangeEnd, from, size);
        return eventService.getEventsForAdmin(getEventsAdminRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByIdForAdmin(
            @PathVariable @Min(1) Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest
    ) {
        log.info("PATCH updateEventByIdForAdmin: eventId={}, {}", eventId, updateEventAdminRequest);
        return eventService.updateEventByIdForAdmin(eventId, updateEventAdminRequest);
    }
}
