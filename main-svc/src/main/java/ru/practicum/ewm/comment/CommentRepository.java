package ru.practicum.ewm.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    Page<Comment> findByEventIdAndAuthorId(Long eventId, Long authorId, Pageable pageable);

    Long countByEventId(Long eventId);

    @Query("""
            SELECT c.event.id, COUNT(c.id)
            FROM Comment c
            WHERE c.event.id IN (:eventIds)
            GROUP BY c.event.id
            """)
    List<Object[]> countCommentsGroupedByEventsRaw(@Param("eventIds") List<Long> eventIds);

    default Map<Long, Long> countCommentsGroupedByEvents(List<Long> eventIds) {
        return countCommentsGroupedByEventsRaw(eventIds)
                .stream()
                .collect(Collectors.toMap(
                        obj -> (Long) obj[0],  // eventId
                        obj -> (Long) obj[1]   // commentCount
                ));
    }
}
