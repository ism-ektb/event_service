package ru.yandex.workshop.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.workshop.dto.LocationDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private String createdDateTime;
    private String startDateTime;
    private String endDateTime;
    private LocationDto location;
    private Long ownerId;
    private String registrationStatus;
    private Long plimit;
}
