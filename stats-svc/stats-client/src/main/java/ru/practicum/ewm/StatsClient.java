package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.List;
import java.util.Optional;

public interface StatsClient {

    void createEndpointHit(EndpointHitCreateDto endpointHitCreateDto);

    List<ViewStatsDto> getStats(String start, String end, Optional<List<String>> uris, Boolean unique);
}
