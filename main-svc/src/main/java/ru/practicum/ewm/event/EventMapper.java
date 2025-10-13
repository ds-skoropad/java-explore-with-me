package ru.practicum.ewm.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventLocation;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static LocationDto toLocationDto(EventLocation eventLocation) {
        return new LocationDto(eventLocation.getLat(), eventLocation.getLon());
    }

    public static EventLocation toEventLocation(LocationDto locationDto) {
        return new EventLocation(locationDto.lat(), locationDto.lon());
    }

    public static EventShortDto toEventShortDto(Event event, Long views, Long comments) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views,
                comments
        );
    }

    public static List<EventShortDto> toManyEventShortDto(List<Event> events, Map<Long, Long> views,
                                                          Map<Long, Long> comments) {
        return events.stream()
                .map(event -> EventMapper.toEventShortDto(
                        event,
                        views.getOrDefault(event.getId(), 0L),
                        comments.getOrDefault(event.getId(), 0L))
                )
                .toList();
    }

    public static EventFullDto toEventFullDto(Event event, Long views, Long comments) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                EventMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                views,
                comments
        );
    }

    public static List<EventFullDto> toManyEventFullDto(List<Event> events, Map<Long, Long> views,
                                                        Map<Long, Long> comments) {
        return events.stream()
                .map(event -> EventMapper.toEventFullDto(
                        event,
                        views.getOrDefault(event.getId(), 0L),
                        comments.getOrDefault(event.getId(), 0L))
                )
                .toList();
    }

    public static void patchEventForAdmin(Event event, UpdateEventAdminRequest request) {
        if (request.annotation() != null) event.setAnnotation(request.annotation());
        if (request.description() != null) event.setDescription(request.description());
        if (request.eventDate() != null) event.setEventDate(request.eventDate());
        if (request.location() != null) event.setLocation(EventMapper.toEventLocation(request.location()));
        if (request.paid() != null) event.setPaid(request.paid());
        if (request.participantLimit() != null) event.setParticipantLimit(request.participantLimit());
        if (request.requestModeration() != null) event.setRequestModeration(request.requestModeration());
        if (request.title() != null) event.setTitle(request.title());
    }

    public static void patchEventForUser(Event event, UpdateEventUserRequest request) {
        if (request.annotation() != null) event.setAnnotation(request.annotation());
        if (request.description() != null) event.setDescription(request.description());
        if (request.eventDate() != null) event.setEventDate(request.eventDate());
        if (request.location() != null) event.setLocation(EventMapper.toEventLocation(request.location()));
        if (request.paid() != null) event.setPaid(request.paid());
        if (request.participantLimit() != null) event.setParticipantLimit(request.participantLimit());
        if (request.requestModeration() != null) event.setRequestModeration(request.requestModeration());
        if (request.title() != null) event.setTitle(request.title());
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User user) {
        Event event = new Event();
        event.setAnnotation(newEventDto.annotation());
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.description());
        event.setEventDate(newEventDto.eventDate());
        event.setInitiator(user);
        event.setLocation(EventMapper.toEventLocation(newEventDto.location()));
        event.setPaid(newEventDto.paid());
        event.setParticipantLimit(newEventDto.participantLimit());
        event.setRequestModeration(newEventDto.requestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(newEventDto.title());
        return event;
    }
}
