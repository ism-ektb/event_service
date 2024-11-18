package ru.yandex.workshop.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.model.Location;

public interface LocationStorage extends JpaRepository<Location, Long> {
}
