package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequesterId(Long requesterId);

    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEventId(Long eventId);
}
