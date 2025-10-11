package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // Public access
    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        return CommentMapper.toCommentDto(findCommentById(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn"));
        return commentRepository.findByEventId(eventId, pageable)
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    // Admin access
    @Override
    public CommentDto updateCommentForAdmin(Long commentId, UpdateCommentDto dto) {
        Comment comment = findCommentById(commentId);
        checkAndUpdateComment(comment, dto);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentForAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
        log.info("Delete comment: id = {}", commentId);
    }

    // Private access
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForUser(Long userId, Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn"));
        return commentRepository.findByEventIdAndAuthorId(eventId, userId, pageable)
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @Override
    public CommentDto createCommentForUser(Long userId, Long eventId, NewCommentDto dto) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Only comment on published events..");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(dto, event, user));
        log.info("Create comment: {}", comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateCommentForUser(Long userId, Long commentId, UpdateCommentDto dto) {
        Comment comment = findCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only the author can edit the comment.");
        }
        checkAndUpdateComment(comment, dto);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentForUser(Long userId, Long commentId) {
        Comment comment = findCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only the author can delete the comment.");
        }
        commentRepository.deleteById(commentId);
        log.info("Delete comment: id = {}", commentId);
    }

    // Additional
    private void checkAndUpdateComment(Comment comment, UpdateCommentDto dto) {
        if (!comment.getText().equals(dto.text())) {
            CommentMapper.patchComment(comment, dto);
            comment.setUpdatedOn(LocalDateTime.now());
            comment = commentRepository.save(comment);
            log.info("Update comment: {}", comment);
        }
    }

    // Additional find-repository
    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment not found: id = %d", commentId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found: id = %d", eventId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found: id = %d", userId)));
    }
}
