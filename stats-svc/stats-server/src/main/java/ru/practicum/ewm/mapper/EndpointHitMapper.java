package ru.practicum.ewm.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.EndpointHitCreateDto;
import ru.practicum.ewm.model.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitCreateDto endpointHitCreateDto) {
        return new EndpointHit(
                0L,
                endpointHitCreateDto.app(),
                endpointHitCreateDto.uri(),
                endpointHitCreateDto.ip(),
                endpointHitCreateDto.timestamp()
        );
    }
}
