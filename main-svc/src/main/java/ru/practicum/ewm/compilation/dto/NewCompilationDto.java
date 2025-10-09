package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewCompilationDto(
        Set<Long> events,
        Boolean pinned, // default false
        @NotBlank @Size(min = 1, max = 50)
        String title
) {
    @Override
    public Boolean pinned() {
        return pinned != null && pinned;
    }
}
