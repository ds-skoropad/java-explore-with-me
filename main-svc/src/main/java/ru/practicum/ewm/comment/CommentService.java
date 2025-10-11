package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size);

    CommentDto updateCommentForAdmin(Long commentId, UpdateCommentDto dto);

    void deleteCommentForAdmin(Long commentId);

    List<CommentDto> getCommentsForUser(Long userId, Long eventId, Integer from, Integer size);

    CommentDto createCommentForUser(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateCommentForUser(Long userId, Long commentId, UpdateCommentDto dto);

    void deleteCommentForUser(Long userId, Long commentId);
}
