package ru.yandex.workshop.service;

import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;

import java.util.List;

public interface EventService {
    EventDto add(Long userId, NewEventDto eventDto);

    EventDto update(Long userId, Long id, NewEventDto eventDto);

    EventDto getById(Long userId, Long id);

    List<EventDto> get(int page, int size, Long ownerId);

    void delete(Long userId, Long id);
}
