package ru.practicum.ewm.dto;

public record ViewStatsDto(
        String app,
        String uri,
        Long hits
) {
}
