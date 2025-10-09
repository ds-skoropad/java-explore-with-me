package ru.practicum.ewm.event.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        @NotNull @Min(-90) @Max(90)
        Float lat,
        @NotNull @Min(-180) @Max(180)
        Float lon
) {
}
