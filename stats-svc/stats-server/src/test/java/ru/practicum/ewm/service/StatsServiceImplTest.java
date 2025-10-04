package ru.practicum.ewm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.exception.NotValidException;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private EndpointHitRepository endpointHitRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    private final LocalDateTime TIMESTAMP = LocalDateTime.of(2022, 9, 6, 11, 0, 23);
    final List<ViewStatsDto> expected = List.of(
            new ViewStatsDto("test-app-1", "/events/1", 1L),
            new ViewStatsDto("test-app-2", "/events/2", 2L)
    );

    @Test
    void createEndpointHit() {
        final String APP = "ewm-main-service";
        final String URI = "/events/1";
        final String IP = "192.163.0.1";

        final EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(APP, URI, IP, TIMESTAMP);
        final EndpointHit endpointHit = new EndpointHit(0L, APP, URI, IP, TIMESTAMP);

        when(endpointHitRepository.save(any())).thenReturn(endpointHit);
        statsService.createEndpointHit(endpointHitCreateDto);

        verify(endpointHitRepository).save(any());
    }

    @Test
    void getStats_whenNotUnique() {
        when(endpointHitRepository.findAllStats(any(), any(), any())).thenReturn(expected);
        List<ViewStatsDto> result = statsService.getStats(TIMESTAMP, TIMESTAMP, List.of(), false);

        assertThat(result)
                .first()
                .hasFieldOrPropertyWithValue("app", "test-app-1")
                .hasFieldOrPropertyWithValue("uri", "/events/1")
                .hasFieldOrPropertyWithValue("hits", 1L);
        verify(endpointHitRepository).findAllStats(any(), any(), any());
        verify(endpointHitRepository, never()).findAllUniqueStats(any(), any(), any());
    }

    @Test
    void getStats_whenUnique() {
        when(endpointHitRepository.findAllUniqueStats(any(), any(), any())).thenReturn(expected);
        List<ViewStatsDto> result = statsService.getStats(TIMESTAMP, TIMESTAMP, List.of(), true);

        assertThat(result)
                .first()
                .hasFieldOrPropertyWithValue("app", "test-app-1")
                .hasFieldOrPropertyWithValue("uri", "/events/1")
                .hasFieldOrPropertyWithValue("hits", 1L);
        verify(endpointHitRepository).findAllUniqueStats(any(), any(), any());
        verify(endpointHitRepository, never()).findAllStats(any(), any(), any());
    }

    @Test
    void getStats_whenStartIsAfterEnd_thenException() {
        assertThatThrownBy(() -> statsService.getStats(TIMESTAMP, TIMESTAMP.minusYears(1), List.of(), false))
                .isInstanceOf(NotValidException.class)
                .hasMessageContaining("The start date cannot be after the end date");
        verify(endpointHitRepository, never()).findAllStats(any(), any(), any());
        verify(endpointHitRepository, never()).findAllUniqueStats(any(), any(), any());
    }
}