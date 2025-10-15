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
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @PatchMapping()
    public CommentDto updateCommentByIdForAdmin(
            @RequestBody @Valid UpdateCommentDto dto) {
        log.info("PATCH updateCommentByIdForAdmin: {}", dto);
        return commentService.updateCommentForAdmin(dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByIdForAdmin(@PathVariable @Min(1) Long commentId) {
        log.info("DELETE deleteCommentByIdForAdmin: commentId={}", commentId);
        commentService.deleteCommentForAdmin(commentId);
    }
}
