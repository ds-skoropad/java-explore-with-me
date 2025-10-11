package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotValidException;
import ru.practicum.ewm.helper.StatsHelper;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestStatus;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatsHelper statsHelper;

    // Public access
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(GetEventsPublicRequest request) {
        validDateRange(request.rangeStart(), request.rangeEnd());
        Sort sort = EventSort.EVENT_DATE.equals(request.sort()) ? Sort.by("eventDate") : Sort.unsorted();
        Pageable pageable = PageRequest.of(request.from() / request.size(), request.size(), sort);

        Specification<Event> spec = Specification.where(null);
        if (request.text() != null) {
            spec = spec.and(EventSpecification.likeText(request.text()));
        }
        if (request.categories() != null && !request.categories().isEmpty()) {
            spec = spec.and(EventSpecification.inCategory(request.categories()));
        }
        if (request.paid() != null) {
            spec = spec.and(EventSpecification.hasPaid(request.paid()));
        }
        if (request.rangeStart() != null && request.rangeEnd() != null) {
            spec = spec.and(EventSpecification.startAfter(request.rangeStart()));
            spec = spec.and(EventSpecification.endBefore(request.rangeEnd()));
        } else {
            spec = spec.and(EventSpecification.startAfter(LocalDateTime.now()));
        }
        if (request.onlyAvailable().equals(Boolean.TRUE)) {
            spec = spec.and(EventSpecification.onlyAvailable());
        }

        Page<Event> events = eventRepository.findAll(spec, pageable);
        List<EventShortDto> result = statsHelper.toManyEventShortDto(events.toList());
        if (EventSort.VIEWS.equals(request.sort())) {
            result.sort(Comparator.comparing(EventShortDto::views));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Long eventId) {
        Event event = findEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new NotFoundException(String.format("Event not published: id = %d", eventId));
        return statsHelper.toEventFullDto(event);
    }

    // Admin access
    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsForAdmin(GetEventsAdminRequest request) {
        validDateRange(request.rangeStart(), request.rangeEnd());
        Pageable pageable = PageRequest.of(request.from() / request.size(), request.size(), Sort.by("id"));

        Specification<Event> spec = Specification.where(null);
        if (request.users() != null && !request.users().isEmpty()) {
            spec = spec.and(EventSpecification.inUsers(request.users()));
        }
        if (request.states() != null && !request.states().isEmpty()) {
            spec = spec.and(EventSpecification.inStates(request.states()));
        }
        if (request.categories() != null && !request.categories().isEmpty()) {
            spec = spec.and(EventSpecification.inCategory(request.categories()));
        }
        if (request.rangeStart() != null && request.rangeEnd() != null) {
            spec = spec.and(EventSpecification.startAfter(request.rangeStart()));
            spec = spec.and(EventSpecification.endBefore(request.rangeEnd()));
        } else {
            spec = spec.and(EventSpecification.startAfter(LocalDateTime.now()));
        }

        Page<Event> events = eventRepository.findAll(spec, pageable);
        return statsHelper.toManyEventFullDto(events.toList());
    }

    @Override
    public EventFullDto updateEventByIdForAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = findEventById(eventId);
        if (request.stateAction() != null) {
            if (request.stateAction() == StateAction.PUBLISH_EVENT) {
                validEventDateByHours(request.eventDate(), 1);
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Can only be published in pending state.");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (request.stateAction() == StateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Cannot reject a published event.");
                }
                event.setState(EventState.CANCELED);
            }
        }
        if (request.category() != null && !request.category().equals(event.getCategory().getId())) {
            event.setCategory(findCategoryById(request.category()));
        }
        EventMapper.patchEventForAdmin(event, request);
        Event updateEvent = eventRepository.save(event);
        log.info("Update event for admin: {}", updateEvent);
        return statsHelper.toEventFullDto(updateEvent);
    }

    // User access
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsForUser(Long userId, Integer from, Integer size) {
        User user = findUserById(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        Page<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        return statsHelper.toManyEventShortDto(events.toList());
    }

    @Override
    public EventFullDto createEventForUser(Long userId, NewEventDto newEventDto) {
        validEventDateByHours(newEventDto.eventDate(), 2);
        User user = findUserById(userId);
        Category category = findCategoryById(newEventDto.category());
        Event event = EventMapper.toEvent(newEventDto, category, user);
        Event createEvent = eventRepository.save(event);
        log.info("Create event for user: {}", createEvent);
        return statsHelper.toEventFullDto(createEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdForUser(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The user is not the owner of the event.");
        }
        return statsHelper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByIdForUser(Long userId, Long eventId, UpdateEventUserRequest request) {
        validEventDateByHours(request.eventDate(), 2);
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The user is not the owner of the event.");
        }
        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new ConflictException("Only change CANCELED or PENDING events");
        }
        if (request.category() != null && !request.category().equals(event.getCategory().getId())) {
            event.setCategory(findCategoryById(request.category()));
        }
        EventMapper.patchEventForUser(event, request);
        if (request.stateAction() != null) {
            event.setState(request.stateAction().equals(StateAction.SEND_TO_REVIEW)
                    ? EventState.PENDING : EventState.CANCELED);
        }
        Event updateEvent = eventRepository.save(event);
        log.info("Update event for user: {}", updateEvent);
        return statsHelper.toEventFullDto(updateEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequestsForUser(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The user is not the owner of the event.");
        }
        return requestRepository.findByEventId(eventId).stream().map(RequestMapper::toParticipationRequestDto).toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestsForUser(Long userId, Long eventId, EventRequestStatusUpdateRequest requestDto) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The user is not the owner of the event.");
        }
        if (requestDto.status().equals(EventRequestStatus.CONFIRMED) && event.getParticipantLimit() > 0
                && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached.");
        }

        List<Request> requests = requestRepository.findAllById(requestDto.requestIds());
        if (requestDto.requestIds().size() != requests.size()) {
            throw new NotFoundException("Part of the requests is missing.");
        }
        if (requests.stream().anyMatch(r -> r.getStatus() != RequestStatus.PENDING)) {
            throw new ConflictException("Not all requests have the status PENDING.");
        }

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        switch (requestDto.status()) {
            case REJECTED -> {
                for (Request request : requests) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejected.add(RequestMapper.toParticipationRequestDto(request));
                }
            }
            case CONFIRMED -> {
                for (Request request : requests) {
                    if (event.getParticipantLimit() - event.getConfirmedRequests() > 0) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmed.add(RequestMapper.toParticipationRequestDto(request));
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        rejected.add(RequestMapper.toParticipationRequestDto(request));
                    }
                }
            }
        }
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult(confirmed, rejected);
        List<Request> saveRequests = requestRepository.saveAll(requests);
        log.info("Update requests: ids={}", saveRequests.stream().map(Request::getId).toList());
        Event saveEvent = eventRepository.save(event);
        log.info("Update event: {}", event);
        return updateResult;
    }

    // Additional valid
    private void validDateRange(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new NotValidException("The StartDate must be after the EndDate.");
        }
    }

    private void validEventDateByHours(LocalDateTime eventDate, Integer hours) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new ConflictException(
                    String.format("The start date must be at least %d hour after the publication date.", hours));
        }
    }

    // Additional find-repository
    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found: id = %d", eventId)));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Category not found: id = %d", categoryId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found: id = %d", userId)));
    }
}
