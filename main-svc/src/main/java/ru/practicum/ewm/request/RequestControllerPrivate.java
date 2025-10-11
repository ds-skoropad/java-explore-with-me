package ru.practicum.ewm.request;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsForUser(@PathVariable @Min(1) Long userId) {
        return requestService.getRequestsForUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequestForUser(
            @PathVariable @Min(1) Long userId,
            @RequestParam @Min(1) Long eventId) {
        log.info("POST createRequestForUser: userId={}, eventId={}", userId, eventId);
        return requestService.createRequestForUser(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long requestId) {
        log.info("PATCH cancelRequestForUser: userId={}, requestId={}", userId, requestId);
        return requestService.cancelRequestForUser(userId, requestId);
    }
}
