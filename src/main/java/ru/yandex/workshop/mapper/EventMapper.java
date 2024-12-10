package ru.yandex.workshop.mapper;

import jakarta.annotation.PostConstruct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.RegistrationStatus;

/*
@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEntity(NewEventDto newEventDto);

    @Mapping(target = "createdDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "startDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "endDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventDto toDto(Event event);
}
 */
