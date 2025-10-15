package ru.practicum.ewm.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getEvent().getId(),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getCreatedOn(),
                comment.getUpdatedOn()
        );
    }

    public static Comment toComment(NewCommentDto dto, Event event, User author) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreatedOn(LocalDateTime.now());
        return comment;
    }

    public static void patchComment(Comment comment, UpdateCommentDto dto) {
        if (dto.text() != null) comment.setText(dto.text());
    }
}
