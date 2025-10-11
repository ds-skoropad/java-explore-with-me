package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.practicum.ewm.valid.IpAddress;

import java.time.LocalDateTime;

public record EndpointHitCreateDto(
        @NotBlank
        String app,
        @NotBlank
        String uri,
        @NotBlank @IpAddress
        String ip,
        @NotNull @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {
}
