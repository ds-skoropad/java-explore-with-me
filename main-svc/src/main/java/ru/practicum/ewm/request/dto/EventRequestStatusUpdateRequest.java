package ru.practicum.ewm.request.dto;

import jakarta.validation.constraints.NotNull;
import ru.practicum.ewm.event.model.EventRequestStatus;

import java.util.List;

public record EventRequestStatusUpdateRequest(
        @NotNull
        List<Long> requestIds,
        @NotNull
        EventRequestStatus status
) {
}
