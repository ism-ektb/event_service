package ru.yandex.workshop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerDto {
    @NotEmpty
    @NotNull
    private Long userId;

    @NotEmpty
    @NotNull
    private Long eventId;

    @NotNull
    @NotEmpty
    private String roleName;
}
