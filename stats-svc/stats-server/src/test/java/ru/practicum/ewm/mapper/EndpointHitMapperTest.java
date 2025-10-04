package ru.practicum.ewm.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointHitMapperTest {
    private static final String APP = "ewm-main-service";
    private static final String URI = "/events/1";
    private static final String IP = "192.163.0.1";
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2022,9,6,11,0,23);

    @Test
    void toEndpointHit() {
        EndpointHitCreateDto endpointHitCreateDto = new EndpointHitCreateDto(APP, URI, IP, TIMESTAMP);
        EndpointHit endpointHit = new EndpointHit(0L, APP, URI, IP, TIMESTAMP);
        EndpointHit mapEndpointHit = EndpointHitMapper.toEndpointHit(endpointHitCreateDto);

        assertThat(mapEndpointHit).isEqualTo(endpointHit);
    }

}