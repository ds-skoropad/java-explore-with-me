package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.request.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record ParticipationRequestDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime created,
        Long event,
        Long id,
        Long requester,
        RequestStatus status // PENDING, CONFIRMED, REJECTED
) {
}
