package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    // User access
    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForUser(Long userId) {
        findUserById(userId);
        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto createRequestForUser(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("The event is not published");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Own event");
        }
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("The request already exists");
        }
        if (event.getParticipantLimit() > 0) {
            Integer count = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (count >= event.getParticipantLimit()) {
                throw new ConflictException("Participant limit reached");
            }
        }
        RequestStatus status = (!event.getRequestModeration() || event.getParticipantLimit() == 0)
                ? RequestStatus.CONFIRMED : RequestStatus.PENDING;
        Request request = RequestMapper.toRequest(user, event, status);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestForUser(Long userId, Long requestId) {
        Request request = findRequestByIdAndRequestorId(requestId, userId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("The user is not the owner of the request");
        }
        request.setStatus(RequestStatus.CANCELED);
        Request cancelRequest = requestRepository.save(request);
        log.info("Canceled request: {}", cancelRequest);
        return RequestMapper.toParticipationRequestDto(cancelRequest);
    }

    // Additional
    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found: id = %d", userId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found: id = %d", eventId)));
    }

    private Request findRequestByIdAndRequestorId(Long requestId, Long requestorId) {
        return requestRepository.findByIdAndRequesterId(requestId, requestorId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Request not found: requestId = %d, requestorId = %d", requestId, requestorId)));
    }
}
