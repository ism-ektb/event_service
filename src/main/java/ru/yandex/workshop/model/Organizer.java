package ru.yandex.workshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "organizers")
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizer_id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "organizer_role_id", referencedColumnName = "role_id")
    private OrganizerRole organizerRole;

    public Organizer(Long organizerId, Event event,  OrganizerRole organizerRole) {
        setEvent(event);
        setUserId(organizerId);
        setOrganizerRole(organizerRole);
    }

    private String roleToString(OrganizerRole organizerRole) {
        String str = "\"role\": { \"id\": " + organizerRole.getId()
                + (organizerRole.getName() == null ? "" : ", \"name\": \"" + organizerRole.getName()) + "\"},";
        return str;
    }
}
