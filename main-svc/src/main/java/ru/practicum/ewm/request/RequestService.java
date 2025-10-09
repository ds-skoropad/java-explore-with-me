package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getRequestsForUser(Long userId);

    ParticipationRequestDto createRequestForUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForUser(Long userId, Long requestId);
}
