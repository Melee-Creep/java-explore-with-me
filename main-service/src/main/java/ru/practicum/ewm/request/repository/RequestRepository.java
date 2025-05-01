package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

   Long countByEventId(Long eventId);

   List<Request> findByRequesterId(Long userId);

   long countByEventIdAndStatus(Long eventId, RequestStatus status);

   List<Request> findAllByEventId(Long eventId);

}