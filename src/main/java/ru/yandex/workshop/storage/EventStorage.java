package ru.yandex.workshop.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.model.Event;

public interface EventStorage extends JpaRepository<Event, Long> {
    Page<Event> findAllByOwnerId(Long ownerId, Pageable pageable);
    Page<Event> findAllByOwnerIdAndRegistrationStatusId(Long ownerId, Long regStatusId, Pageable pageable);
    Page<Event> findAllByRegistrationStatusId(Long regStatusId, Pageable pageable);
}
