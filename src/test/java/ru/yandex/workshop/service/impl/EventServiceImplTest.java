package ru.yandex.workshop.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.exception.ConflictException;
import ru.yandex.workshop.model.Event;
import ru.yandex.workshop.model.Location;
import ru.yandex.workshop.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class EventServiceImplTest {
    @Autowired
    private EventService eventService;
    private final EntityManager em;

    @Test
    void add() {
        NewEventDto newEventDto = new NewEventDto(
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocationDto(66.6f, 100.1f));

        eventService.add(1L, newEventDto);

        TypedQuery<Event> itemQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);
        Event event = itemQuery.setParameter("name", newEventDto.getName()).getSingleResult();

        assertThat(event.getId(), notNullValue());
        assertThat(event.getName(), equalTo(newEventDto.getName()));
        assertThat(event.getDescription(), equalTo(newEventDto.getDescription()));
        assertThat(event.getStartDateTime(), equalTo(newEventDto.getStartDateTime()));
        assertThat(event.getEndDateTime(), equalTo(newEventDto.getEndDateTime()));
        assertThat(event.getLocation(), notNullValue());
    }

    @Test
    void update() {

        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        NewEventDto newEventDto = new NewEventDto(
                "DFfdhgdhghfjjfjggjh",
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(8),
                new LocationDto(66.6f, 100.1f));

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        eventService.update(eventQuery.setParameter("name", event.getName()).getSingleResult().getOwnerId(),
                eventQuery.setParameter("name", event.getName()).getSingleResult().getId(),
                newEventDto);

        TypedQuery<Event> eventAdQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);
        Event updatedEvent = eventAdQuery.setParameter("name", newEventDto.getName()).getSingleResult();

        assertThat(updatedEvent.getId(), notNullValue());
        assertThat(updatedEvent.getName(), equalTo(newEventDto.getName()));
        assertThat(updatedEvent.getDescription(), equalTo(newEventDto.getDescription()));
        assertThat(updatedEvent.getStartDateTime(), equalTo(newEventDto.getStartDateTime()));
        assertThat(updatedEvent.getEndDateTime(), equalTo(newEventDto.getEndDateTime()));
        assertThat(updatedEvent.getLocation(), notNullValue());
    }

    @Test
    void getById() {
        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        EventDto eventDto = eventService.getById(eventQuery.setParameter("name", event.getName()).getSingleResult().getOwnerId(),
                eventQuery.setParameter("name", event.getName()).getSingleResult().getId());

        assertThat(eventDto.getId(), notNullValue());
        assertThat(eventDto.getName(), equalTo(event.getName()));
        assertThat(eventDto.getDescription(), equalTo(event.getDescription()));
        assertThat(eventDto.getCreatedDateTime(), notNullValue());
        assertThat(eventDto.getStartDateTime(), notNullValue());
        assertThat(eventDto.getEndDateTime(), notNullValue());
        assertThat(eventDto.getLocation(), notNullValue());
    }

    @Test
    void getByIdWithNullCreatedDateTime() {
        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        EventDto eventDto = eventService.getById(100000000L,
                eventQuery.setParameter("name", event.getName()).getSingleResult().getId());

        assertThat(eventDto.getCreatedDateTime(), nullValue());
    }

    @Test
    void getWithoutOwnerId() {
        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        List<EventDto> eventDtos = eventService.get(0, 10, null);

        assertThat(eventDtos, notNullValue());
    }

    @Test
    void getWithOwnerId() {
        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        List<EventDto> eventDtos = eventService.get(0,
                10,
                eventQuery.setParameter("name", event.getName()).getSingleResult().getOwnerId());

        assertThat(eventDtos, notNullValue());
    }

    @Test
    void deleteWithWrongOwner() {
        em.persist(new Location(null,66f, 100f));
        em.flush();

        TypedQuery<Location> locationQuery = em.createQuery("select l from Location as l where l.lat = :lat", Location.class);

        Event event = new Event(null,
                "Квадроциклы",
                "Лучшие квадроциклы в городе",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                locationQuery.setParameter("lat", 66f).getSingleResult(),
                1L);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                eventService.delete(1000000000000L,
                        eventQuery.setParameter("name", event.getName()).getSingleResult().getId()));

        assertEquals("Удалить событие может только создатель", exception.getMessage());
    }
}