package ru.yandex.workshop.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.workshop.dto.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 5, max = 2000, message = "Название может содержать от 5 до 2000 символов")
    private String name;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание может содержать от 20 до 7000 символов")
    private String description;
    @FutureOrPresent(message = "startDateTime может быть в будущем или в настоящем времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;
    @FutureOrPresent(message = "endDateTime может быть в будущем или в настоящем времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;
    @NotNull
    private LocationDto location;

    private Long plimit;
}
