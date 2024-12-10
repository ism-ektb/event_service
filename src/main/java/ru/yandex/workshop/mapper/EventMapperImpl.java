package ru.yandex.workshop.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.RegistrationStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@RequiredArgsConstructor
public class EventMapperImpl {
/*    public static Event fromDtoToEvent(EventDto dto, RegistrationStatus registrationStatus) {
        return new Event(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dateTime(dto.getCreatedDateTime()),
                dateTime(dto.getStartDateTime()),
                dateTime(dto.getEndDateTime()),
                locationMapper.toEntity(dto.getLocation()),
                dto.getOwnerId(),
                null,
                registrationStatus);
    } */

    public static Event fromNewDtoToEvent(NewEventDto newDto) {
        Event event = new Event();
        event.setName(newDto.getName());
        event.setDescription(newDto.getDescription());
        event.setStartDateTime(newDto.getStartDateTime());
        event.setEndDateTime(newDto.getEndDateTime());
        event.setParticipantsLimit(newDto.getParticipantsLimit());
        return event;
    }

    public static EventDto fromEventToDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()),
                event.getOwnerId(),
                event.getRegistrationStatus().getName(),
                event.getParticipantsLimit()
        );
    }

    private static LocalDateTime dateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
