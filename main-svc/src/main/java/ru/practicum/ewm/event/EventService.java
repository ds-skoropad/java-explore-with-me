package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getEvents(GetEventsPublicRequest getEventsPublicRequest);

    EventFullDto getEventById(Long eventId, String uri, String ip);

    List<EventFullDto> getEventsForAdmin(GetEventsAdminRequest getEventsAdminRequest);

    EventFullDto updateEventByIdForAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEventsForUser(Long userId, Integer from, Integer size);

    EventFullDto createEventForUser(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByIdForUser(Long userId, Long eventId);

    EventFullDto updateEventByIdForUser(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getEventRequestsForUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsForUser(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
