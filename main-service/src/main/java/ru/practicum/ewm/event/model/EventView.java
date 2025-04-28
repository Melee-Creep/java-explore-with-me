package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_views")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private String ip;
}

