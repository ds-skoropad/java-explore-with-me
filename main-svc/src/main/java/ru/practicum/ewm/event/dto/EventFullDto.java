package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record EventFullDto(
        String annotation,
        CategoryDto category,
        Long confirmedRequests,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime createdOn,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        Long id,
        UserShortDto initiator,
        LocationDto location,
        Boolean paid, // default = false
        Integer participantLimit, // default = 0
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime publishedOn,
        Boolean requestModeration, // default = true
        EventState state,
        String title,
        Long views,
        Long comments
) {
}
