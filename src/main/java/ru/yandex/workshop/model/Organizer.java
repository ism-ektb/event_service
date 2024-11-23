package ru.yandex.workshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizers")
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizer_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "organizer_role_id", referencedColumnName = "role_id")
    private OrganizerRole organizerRole;

    public Organizer(Event event, Long userId, OrganizerRole organizerRole) {
        this.setEvent(event);
        this.setUserId(userId);
        this.setOrganizerRole(organizerRole);
    }

    private String roleToString(OrganizerRole organizerRole) {
        String str = "\"role\": { \"id\": " + organizerRole.getId()
                + (organizerRole.getName() == null ? "" : ", \"name\": \"" + organizerRole.getName()) + "\"},";
        return str;
    }
}
