package ru.yandex.workshop.model;
import jakarta.persistence.*;
import lombok.*;

/*
Исполнитель,
Менеджер
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizer_roles")
public class OrganizerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    private String name;
}
