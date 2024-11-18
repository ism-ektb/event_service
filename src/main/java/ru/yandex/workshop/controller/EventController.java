package ru.yandex.workshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.service.EventService;
import ru.yandex.workshop.util.ApiPathConstants;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.EVENTS_PATH)
public class EventController {
    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Создание нового события")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody NewEventDto eventDto) {
        return eventService.add(userId, eventDto);
    }

    @Operation(summary = "Обновление события по ее id", description = "Обновлять событие может только создатель задачи")
    @PatchMapping(ApiPathConstants.ID_PATH)
    public EventDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable("id") long id,
                           @Valid @RequestBody NewEventDto eventDto) {
        return eventService.update(userId, id, eventDto);
    }

    @Operation(summary = "Поиск события по id", description = "Создатель события получает данные с полем createdDateTime")
    @GetMapping(ApiPathConstants.ID_PATH)
    public EventDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id) {
        return eventService.getById(userId, id);
    }

    @Operation(summary = "Получение событий на страницах", description = "Есть необязательный фильтр по id создателя")
    @GetMapping
    public List<EventDto> get(@RequestParam(defaultValue = "0", name = "page") int page,
                              @RequestParam(defaultValue = "10", name = "size") int size,
                              @RequestParam(required = false, name = "ownerId") Long ownerId) {
        return eventService.get(page, size, ownerId);
    }

    @Operation(summary = "Удаление события", description = "Удалить событие может только создатель")
    @DeleteMapping(ApiPathConstants.ID_PATH)
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id) {
        eventService.delete(userId, id);
    }
}
