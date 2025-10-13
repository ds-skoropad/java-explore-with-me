package ru.practicum.ewm.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.MainConstants.DATE_TIME_FORMATTER;
import static ru.practicum.ewm.MainConstants.EWM_APP_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsHelper {
    private final StatsClient statsClient;

    public void createEndpointHit(String uri, String ip) {
        EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(
                EWM_APP_NAME,
                uri,
                ip,
                LocalDateTime.now()
        );
        log.info("Stats-Client createEndpointHit: {}", endpointHitCreateDto);
        statsClient.createEndpointHit(endpointHitCreateDto);
    }

    public Long getViews(Event event) {
        return getManyViews(List.of(event)).getOrDefault(event.getId(), 0L);
    }

    public Map<Long, Long> getManyViews(List<Event> events) {
        LocalDateTime minDate = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        if (minDate == null) return Collections.emptyMap();
        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();
        List<ViewStatsDto> stats = statsClient.getStats(
                minDate.format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                Optional.of(uris),
                true);
        Map<String, Long> statsMap = stats.stream()
                .filter(dto -> dto.app().equals(EWM_APP_NAME))
                .collect(Collectors.toMap(ViewStatsDto::uri, ViewStatsDto::hits));
        return events.stream()
                .collect(Collectors.toMap(
                        Event::getId,
                        event -> statsMap.getOrDefault("/events/" + event.getId(), 0L))
                );
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventMapper.toEventFullDto(event, getViews(event), null);
    }

    public List<EventFullDto> toManyEventFullDto(List<Event> events) {
        Map<Long, Long> views = getManyViews(events);
        return events.stream()
                .map(event -> EventMapper.toEventFullDto(event, views.getOrDefault(event.getId(), 0L), null))
                .toList();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventMapper.toEventShortDto(event, getViews(event), null);
    }

    public List<EventShortDto> toManyEventShortDto(List<Event> events) {
        Map<Long, Long> views = getManyViews(events);
        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event, views.getOrDefault(event.getId(), 0L), null))
                .toList();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationMapper.toCompilationDto(
                compilation,
                new HashSet<>(toManyEventShortDto(compilation.getEvents().stream().toList())));
    }

    public List<CompilationDto> toManyCompilationDto(List<Compilation> compilations) {
        List<Event> events = compilations.stream()
                .map(Compilation::getEvents)
                .flatMap(Set::stream)
                .toList();
        Map<Long, Long> views = getManyViews(events);
        return compilations.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(
                        compilation,
                        compilation.getEvents().stream()
                                .map(event -> EventMapper.toEventShortDto(
                                        event,
                                        views.getOrDefault(event.getId(), 0L), null))
                                .collect(Collectors.toSet())))
                .toList();
    }
}
