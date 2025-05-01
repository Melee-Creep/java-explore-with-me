package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.EventView;

@Repository
public interface EventViewRepository extends JpaRepository<EventView, Long> {

    boolean existsByEventIdAndIp(Long eventId, String ip);
}