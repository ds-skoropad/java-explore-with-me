package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record NewEventDto(
        @NotBlank @Size(min = 20, max = 2000)
        String annotation,
        @NotNull
        Long category,
        @NotBlank @Size(min = 20, max = 7000)
        String description,
        @NotNull @Future @JsonFormat(pattern = DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        @NotNull
        LocationDto location,
        Boolean paid, // default = false
        @Min(0)
        Integer participantLimit, // default = 0
        Boolean requestModeration, // default = true
        @NotBlank @Size(min = 3, max = 120)
        String title
) {

    @Override
    public Boolean paid() {
        return paid != null && paid;
    }

    @Override
    public Integer participantLimit() {
        return participantLimit == null ? 0 : participantLimit;
    }

    @Override
    public Boolean requestModeration() {
        return requestModeration == null || requestModeration;
    }
}
