package ru.yandex.workshop.mapper;

import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.Organizer;
import ru.yandex.workshop.model.OrganizerRole;

public class OrganizerMapper {
    public static Organizer fromDtoToOrganizer(OrganizerDto dto, OrganizerRole organizerRole,
                                               Event event) {
        return new Organizer(event, dto.getUserId(), organizerRole);
    }

    public static OrganizerDto fromOrganizerToDto(Organizer organizer) {
        return new OrganizerDto(organizer.getEvent().getId(), organizer.getUserId(),
                organizer.getOrganizerRole().getName());
    }
}
