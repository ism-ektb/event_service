package ru.yandex.workshop.mapper;

import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.Organizer;
import ru.yandex.workshop.model.OrganizerRole;

public class OrganizerMapper {
    public static Organizer fromDtoToOrganizer(OrganizerDto dto, OrganizerRole organizerRole,
                                               Event event) {
        return new Organizer(dto.getUserId(), event, organizerRole);
    }

    public static OrganizerDto fromOrganizerToDto(Organizer organizer) {
        return new OrganizerDto(organizer.getUserId(), organizer.getEvent().getId(),
                organizer.getOrganizerRole().getName());
    }
}
