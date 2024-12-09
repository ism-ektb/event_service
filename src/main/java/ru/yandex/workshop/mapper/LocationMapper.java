package ru.yandex.workshop.mapper;

import org.mapstruct.Mapper;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toEntity(LocationDto location);

    LocationDto toDto(Location location);
}
