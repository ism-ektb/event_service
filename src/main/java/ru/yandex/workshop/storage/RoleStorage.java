package ru.yandex.workshop.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.model.OrganizerRole;

public interface RoleStorage extends JpaRepository<OrganizerRole, Long> {
    OrganizerRole findByName(String name);
}
