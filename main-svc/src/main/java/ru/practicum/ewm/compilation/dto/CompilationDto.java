package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Set;

public record CompilationDto(
        Set<EventShortDto> events,
        Long id,
        Boolean pinned,
        String title
) {
}

