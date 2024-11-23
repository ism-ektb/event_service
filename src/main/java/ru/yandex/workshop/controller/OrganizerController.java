package ru.yandex.workshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.service.OrganizerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class OrganizerController {
    private final OrganizerService organizerService;

    @Operation(summary = "Добавление пользователя в команду организаторов события по еvent id",
            description = "Добавлять пользователя может владелец проекта или участник с ролью менеджера")
    @PostMapping("/{eventId}/organizers/{organizerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizerDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable("eventId") long eventId,
                            @PathVariable("organizerId") long organizerId,
                            @RequestParam (name = "role") String role) {
        return organizerService.add(userId, eventId, organizerId, role);
    }

    @Operation(summary = "Удаление пользователя из команды организаторов события",
            description = "Удалять пользователя может владелец проекта или участник с ролью менеджера")
    @DeleteMapping("/{eventId}/organizers/{organizerId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable("organizerId") long organizerId,
                       @PathVariable("eventId") long eventId) {
        organizerService.delete(userId, organizerId, eventId);
    }

    @Operation(summary = "Обновление роли пользователя",
            description = "Обновлять роль пользователя может владелец проекта или участник с ролью менеджера")
    @PatchMapping("/{eventId}/organizers/{organizerId}")
    public OrganizerDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable("organizerId") long organizerId,
                           @PathVariable("eventId") long eventId,
                           @RequestParam String role) {
        return organizerService.update(userId, eventId, organizerId, role);
    }

    @Operation(summary = "Получение всех организаторов события",
            description = "Получить всех организаторов события может любой пользователь")
    @GetMapping("/{eventId}/organizers")
    public List<OrganizerDto> getEventOrganizers(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable("eventId") long eventId) {
        return organizerService.getEventOrganizers(eventId);
    }
}
