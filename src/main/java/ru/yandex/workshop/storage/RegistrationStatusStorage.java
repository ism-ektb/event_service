package ru.yandex.workshop.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.model.RegistrationStatus;

public interface RegistrationStatusStorage extends JpaRepository<RegistrationStatus, Long> {
    RegistrationStatus findByName(String name);
}
