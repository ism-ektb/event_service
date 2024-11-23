package ru.yandex.workshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.exception.ConflictException;
import ru.yandex.workshop.exception.NotFoundException;
import ru.yandex.workshop.mapper.OrganizerMapper;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.Organizer;
import ru.yandex.workshop.model.OrganizerRole;
import ru.yandex.workshop.service.OrganizerService;
import ru.yandex.workshop.storage.EventStorage;
import ru.yandex.workshop.storage.OrganizerStorage;
import ru.yandex.workshop.storage.RoleStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    private final OrganizerStorage organizerStorage;
    private final EventStorage eventStorage;
    private final RoleStorage roleStorage;
    @Override
    public OrganizerDto add(long userId, long eventId, long organizerId, String roleName) {
        Event event = eventStorage.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + eventId + " не найдено"));
        OrganizerRole organizerRole = roleStorage.findByName(roleName);
        if (organizerRole == null)
            throw new NotFoundException("Роль " + roleName + " в базе не существует");
        CheckIfUserCanAddDeleteOrganizers(userId, event);
        OrganizerDto dto = new OrganizerDto(organizerId, eventId, roleName);
        Organizer organizer = OrganizerMapper.fromDtoToOrganizer(dto, organizerRole, event);
        return OrganizerMapper.fromOrganizerToDto(organizerStorage.save(organizer));
    }

    private boolean CheckIfUserCanAddDeleteOrganizers(long userId, Event event) {
        List<Organizer> organizers = organizerStorage.findAllByEventId(event.getId());
        if (!(organizers.stream().filter(x -> x.getOrganizerRole().getName().toLowerCase().equals("менеджер"))
                .map(y -> y.getUserId()).anyMatch(z -> z == userId) || userId == event.getOwnerId()))
            throw new ConflictException(
                    "Добавлять членов команды может владелец проекта или участник проекта с ролью менеджер");
        return true;
    }

    @Override
    public void delete(long userId, long organizerId, long eventId) {
        Event event = eventStorage.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + eventId + " не найдено"));
        CheckIfUserCanAddDeleteOrganizers(userId, event);
        organizerStorage.deleteByUserId(organizerId);
    }

    @Override
    public OrganizerDto update(long userId, long eventId, long organizerId, String roleName) {
        Event event = eventStorage.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + eventId + " не найдено"));
        OrganizerRole organizerRole = roleStorage.findByName(roleName);
        if (organizerRole == null)
            throw new NotFoundException("Роль " + roleName + " в базе не существует");
        CheckIfUserCanAddDeleteOrganizers(userId, event);
        Organizer organizer = organizerStorage.findByEventIdAndUserId(eventId, organizerId);
        return OrganizerMapper.fromOrganizerToDto(organizerStorage.save(organizer));
    }

    @Override
    public List<OrganizerDto> getEventOrganizers(long eventId) {
        return organizerStorage.findAllByEventId(eventId).stream()
                .map(x -> OrganizerMapper.fromOrganizerToDto(x)).collect(Collectors.toList());
    }
}
