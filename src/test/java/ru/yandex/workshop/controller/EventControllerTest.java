package ru.yandex.workshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.service.EventService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = EventController.class)
class EventControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    EventService eventService;

    private final EventDto eventDto = new EventDto(1L,
            "Квадроциклы",
            "Лучшие квадроциклы в городе",
            "2026-12-25 04:41:06",
            "2027-12-25 04:41:06",
            "2028-12-25 04:41:06",
            new LocationDto(66f, 100f),
            1L);

    @Test
    void add() throws Exception {
        NewEventDto newEventDto = new NewEventDto("Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocationDto(66, 100));

        when(eventService.add(anyLong(), any())).thenReturn(eventDto);

        String result = mvc.perform(post("/events")
                        .content(mapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(mapper.writeValueAsString(eventDto), result);
    }

    @Test
    void update() throws Exception {
        NewEventDto newEventDto = new NewEventDto("Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocationDto(66f, 100f));

        when(eventService.update(anyLong(), anyLong(), any())).thenReturn(eventDto);

        String result = mvc.perform(patch("/events/1")
                        .content(mapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(mapper.writeValueAsString(eventDto), result);
    }

    @Test
    void getById() throws Exception {
        when(eventService.getById(anyLong(), anyLong())).thenReturn(eventDto);

        String result = mvc.perform(get("/events/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(mapper.writeValueAsString(eventDto), result);
    }

    @Test
    void getList() throws Exception {
        when(eventService.get(anyInt(), anyInt(), anyLong())).thenReturn(Collections.singletonList(eventDto));

        mvc.perform(get("/events?page=0&size=10&ownerId=1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(eventDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(eventDto.getName())))
                .andExpect(jsonPath("$[0].description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$[0].createdDateTime", is(eventDto.getCreatedDateTime())))
                .andExpect(jsonPath("$[0].startDateTime", is(eventDto.getStartDateTime())))
                .andExpect(jsonPath("$[0].endDateTime", is(eventDto.getEndDateTime())))
                .andExpect(jsonPath("$[0].location", notNullValue()))
                .andExpect(jsonPath("$[0].ownerId", notNullValue()));
    }
}