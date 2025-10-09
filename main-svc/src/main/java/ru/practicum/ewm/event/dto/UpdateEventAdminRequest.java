package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.event.model.StateAction;

import java.time.LocalDateTime;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record UpdateEventAdminRequest(
        @Size(min = 20, max = 2000)
        String annotation,
        Long category,
        @Size(min = 20, max = 7000)
        String description,
        @Future @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        LocationDto location,
        Boolean paid,
        @PositiveOrZero
        Integer participantLimit,
        Boolean requestModeration,
        StateAction stateAction, // PUBLISH_EVENT, REJECT_EVENT
        @Size(min = 3, max = 120)
        String title
) {
}
