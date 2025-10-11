package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.CommentService;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentControllerPrivate {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return commentService.getCommentsForUser(userId, eventId, from, size);
    }

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long eventId,
            @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST createCommentForUser: userId={}, eventId={}", userId, eventId);
        return commentService.createCommentForUser(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long commentId,
            @RequestBody @Valid UpdateCommentDto dto) {
        log.info("PATCH updateCommentForUser: userId={}, commentId={}", userId, commentId);
        return commentService.updateCommentForUser(userId, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentForUser(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long commentId) {
        log.info("DELETE deleteCommentForUser: userId={}, commentId={}", userId, commentId);
        commentService.deleteCommentForUser(userId, commentId);
    }
}
