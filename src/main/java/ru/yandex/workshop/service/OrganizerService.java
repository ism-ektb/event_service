package ru.yandex.workshop.service;

import ru.yandex.workshop.dto.OrganizerDto;
import java.util.List;

public interface OrganizerService {
    OrganizerDto add(long userId, long eventId, long organizerId, String role);

    void delete(long userId, long organizerId, long eventId);

    OrganizerDto update(long userId, long eventId, long organizerId, String role);

    List<OrganizerDto> getEventOrganizers(long eventId);
}
