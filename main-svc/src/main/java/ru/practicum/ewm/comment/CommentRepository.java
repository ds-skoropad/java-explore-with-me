package ru.practicum.ewm.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    Page<Comment> findByEventIdAndAuthorId(Long eventId, Long authorId, Pageable pageable);
}
