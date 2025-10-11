package ru.practicum.ewm.event;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public final class EventSpecification {
    public static Specification<Event> likeText(String text) {
        String pattern = "%" + text.toLowerCase() + "%";
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                );
    }

    public static Specification<Event> inCategory(List<Long> categories) {
        return (root, query, criteriaBuilder) ->
                root.get("category").get("id").in(categories);
    }

    public static Specification<Event> hasPaid(Boolean paid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> startAfter(LocalDateTime localDateTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), localDateTime);
    }

    public static Specification<Event> endBefore(LocalDateTime localDateTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), localDateTime);
    }

    public static Specification<Event> onlyAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                        criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
                );
    }

    public static Specification<Event> inUsers(List<Long> users) {
        return (root, query, criteriaBuilder) ->
                root.get("initiator").get("id").in(users);
    }

    public static Specification<Event> inStates(List<String> states) {
        return (root, query, criteriaBuilder) ->
                root.get("state").in(states);
    }
}
