package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.event.model.StateAction;

import java.time.LocalDateTime;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record UpdateEventUserRequest(
        @Size(min = 20, max = 2000)
        String annotation,
        Long category,
        @Size(min = 20, max = 7000)
        String description,
        @Future @JsonFormat(pattern = DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        LocationDto location,
        Boolean paid,
        @Min(0)
        Integer participantLimit,
        Boolean requestModeration,
        StateAction stateAction, // SEND_TO_REVIEW, CANCEL_REVIEW
        @Size(min = 3, max = 120)
        String title
) {
}
