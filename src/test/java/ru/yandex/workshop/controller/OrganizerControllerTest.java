package ru.yandex.workshop.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.service.OrganizerService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = OrganizerController.class)
class OrganizerControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    OrganizerService organizerService;

    private final EventDto eventDto = new EventDto(
            1L,
            "Квадроциклы",
            "Лучшие квадроциклы в городе",
            "2026-12-25 04:41:06",
            "2027-12-25 04:41:06",
            "2028-12-25 04:41:06",
            new LocationDto(66f, 100f),
            1L,
            null,
            6L);

    private final OrganizerDto organizerDto = new OrganizerDto(1, eventDto.getId(), "менеджер");

    @Test
    void add() throws Exception {
        when(organizerService.add(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(organizerDto);

        String result = mvc.perform(post("/events/" + eventDto.getId() + "/organizers/1")
                        .param("role", "менеджер")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(mapper.writeValueAsString(organizerDto), result);
    }

    @Test
    void update() throws Exception {
        when(organizerService.update(anyLong(), anyLong(), anyLong(), any())).thenReturn(organizerDto);

        String result = mvc.perform(patch("/events/" + eventDto.getId() + "/organizers/1")
                        .param("role", "менеджер")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(mapper.writeValueAsString(organizerDto), result);
    }

    @Test
    void getEventOrganizers() throws Exception {
        when(organizerService.getEventOrganizers(anyLong())).thenReturn(Collections.singletonList(organizerDto));

        mvc.perform(get("/events/" + eventDto.getId() + "/organizers")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is(organizerDto.getUserId()), Long.class))
                .andExpect(jsonPath("$[0].eventId", is(organizerDto.getEventId()), Long.class))
                .andExpect(jsonPath("$[0].roleName", is(organizerDto.getRoleName())));
    }
}