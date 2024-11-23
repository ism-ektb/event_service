package ru.yandex.workshop.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.model.Organizer;
import java.util.List;
import java.util.Optional;

public interface OrganizerStorage extends JpaRepository<Organizer, Long> {
    List<Organizer> findAllByEventId(Long eventId);
    void deleteByUserId(Long userId);
    Optional<Organizer> findById(Long id);

    Organizer findByEventIdAndUserId(long eventId, long organizerId);
}
