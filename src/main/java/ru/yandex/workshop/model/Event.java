package ru.yandex.workshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdDateTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;
    private Long ownerId;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Organizer> organizers = new ArrayList<>();
}
