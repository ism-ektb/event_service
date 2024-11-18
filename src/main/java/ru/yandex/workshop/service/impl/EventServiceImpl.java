package ru.yandex.workshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.exception.ConflictException;
import ru.yandex.workshop.exception.NotFoundException;
import ru.yandex.workshop.mapper.EventMapper;
import ru.yandex.workshop.mapper.LocationMapper;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.Location;
import ru.yandex.workshop.service.EventService;
import ru.yandex.workshop.storage.EventStorage;
import ru.yandex.workshop.storage.LocationStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;
    private final LocationStorage locationStorage;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    /**
     * Создание нового события
     * @param userId
     * @param newEventDto
     */
    @Override
    public EventDto add(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEndDateTime().isBefore(newEventDto.getStartDateTime())) {
            throw new ConflictException("Дата конца не может быть раньше даты начала");
        }
        Location locationWithId = locationStorage.save(locationMapper.toEntity(newEventDto.getLocation()));
        Event event = eventMapper.toEntity(newEventDto);
        event.setOwnerId(userId);
        event.setLocation(locationWithId);
        event.setCreatedDateTime(LocalDateTime.now());
        return eventMapper.toDto(eventStorage.save(event));
    }

    /**
     * Обновление события по id. Обновить может только создатель
     * @param userId
     * @param id
     * @param newEventDto
     */
    @Override
    public EventDto update(Long userId, Long id, NewEventDto newEventDto) {
        Event event = eventStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + id + " не найдено"));
        if (event.getOwnerId() != userId) {
            throw new ConflictException("Пользователь с id=" + userId + " не является создателем события с id=" + id
                    + " поэтому не может его обновить");
        }
        if (newEventDto.getStartDateTime() != null && newEventDto.getEndDateTime() != null) {
            if (newEventDto.getEndDateTime().isBefore(newEventDto.getStartDateTime())) {
                throw new ConflictException("Дата конца не может быть раньше даты начала");
            }
        } else if (newEventDto.getStartDateTime() == null && newEventDto.getEndDateTime() != null) {
            if (newEventDto.getEndDateTime().isBefore(event.getStartDateTime())) {
                throw new ConflictException("Дата конца для обновления события не может быть раньше даты начала");
            }
        } else if (newEventDto.getStartDateTime() != null) {
            if (event.getEndDateTime().isBefore(newEventDto.getStartDateTime())) {
                throw new ConflictException("Дата конца не может быть раньше даты начала");
            }
        }

        event.setName(newEventDto.getName() == null ? event.getName() : newEventDto.getName());
        event.setDescription(newEventDto.getDescription() == null
                ? event.getDescription() : newEventDto.getDescription());
        event.setStartDateTime(newEventDto.getStartDateTime() == null
                ? event.getStartDateTime() : newEventDto.getStartDateTime());
        event.setEndDateTime(newEventDto.getEndDateTime() == null
                ? event.getEndDateTime() : newEventDto.getEndDateTime());
        event.setLocation(newEventDto.getLocation() == null ? event.getLocation()
                : locationStorage.save(locationMapper.toEntity(newEventDto.getLocation())));
        return eventMapper.toDto(eventStorage.save(event));
    }

    /**
     * Поиск события по id. Создатель события получить информацию с createdDateTime полем.
     * Другие пользователи без этого поля
     * @param userId
     * @param id
     */
    @Override
    public EventDto getById(Long userId, Long id) {
        Event event = eventStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + id + " не найдено"));
        if (event.getOwnerId() == userId) {
            return eventMapper.toDto(event);
        }
        EventDto eventDto = eventMapper.toDto(event);
        eventDto.setCreatedDateTime(null);
        return eventDto;
    }

    /**
     * Получение событий страницами. Есть необязательный фильтр событий по их создателю
     * @param page
     * @param size
     * @param ownerId
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventDto> get(int page, int size, Long ownerId) {
        PageRequest pageRequest = PageRequest.of(page > 0 ? page / size : 0, size);
        if (ownerId != null) {
            return eventStorage.findAllByOwnerId(ownerId, pageRequest).map(eventMapper::toDto).getContent();
        }
        return eventStorage.findAll(pageRequest).map(eventMapper::toDto).getContent();
    }

    /**
     * Удаление события. Событие может удалить только создатель
     * @param userId
     * @param id
     */
    @Override
    public void delete(Long userId, Long id) {
        Event event = eventStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Event: Событие с id=" + id + " не найдено"));
        if (event.getOwnerId() != userId) {
            throw new ConflictException("Удалить событие может только создатель");
        }
        eventStorage.deleteById(id);
    }
}
