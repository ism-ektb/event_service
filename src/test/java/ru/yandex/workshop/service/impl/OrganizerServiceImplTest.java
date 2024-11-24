package ru.yandex.workshop.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yandex.workshop.dto.LocationDto;
import ru.yandex.workshop.dto.OrganizerDto;
import ru.yandex.workshop.dto.event.EventDto;
import ru.yandex.workshop.dto.event.NewEventDto;
import ru.yandex.workshop.model.Organizer;
import ru.yandex.workshop.service.EventService;
import ru.yandex.workshop.service.OrganizerService;
import ru.yandex.workshop.storage.OrganizerStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OrganizerServiceImplTest {
    @Autowired
    private EventService eventService;

    @Autowired
    private final OrganizerStorage organizerStorage;

    @Autowired
    private OrganizerService organizerService;

    private final EntityManager em;

    private static NewEventDto newEventDto = new NewEventDto(
            "Квадроциклы",
            "Лучшие квадроциклы в городе",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            new LocationDto(66.6f, 100.1f));

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");
//

    @Test
    void add() {
        EventDto ed = eventService.add(1L, newEventDto);

        int countBefore = organizerStorage.findAll().size();
        OrganizerDto orgDto = organizerService.add(1, ed.getId(), 1, "менеджер");
        assertThat(organizerStorage.findAll().size(), equalTo(countBefore + 1));
        Organizer organizer = organizerStorage.findByEventIdAndUserId(ed.getId(),1);
        assertThat(organizer.getId(), notNullValue());
        assertThat(organizer.getOrganizerRole().getName(), equalTo("менеджер"));
        assertThat(organizer.getUserId(), equalTo(1L));
        assertThat(organizer.getEvent().getId(), equalTo(ed.getId()));
    }

    @Test
    void update() {
        EventDto ed = eventService.add(1L, newEventDto);
        organizerService.add(1, ed.getId(), 1, "менеджер");

        OrganizerDto dto = organizerService.update(1, ed.getId(), 1, "исполнитель");
        assertThat(dto.getUserId(), equalTo(1L));
        assertThat(dto.getEventId(), equalTo(ed.getId()));
        assertThat(dto.getRoleName(), equalTo("исполнитель"));
    }

    @Test
    void delete() {
        EventDto ed = eventService.add(1L, newEventDto);
        organizerService.add(1, ed.getId(), 1, "менеджер");

        Organizer organizer = organizerStorage.findByEventIdAndUserId(ed.getId(),1);
        organizerService.delete(1, organizer.getId(), ed.getId());
        organizer = organizerStorage.findByEventIdAndUserId(ed.getId(),1);

        assertEquals(organizer, null);
    }

/*    @Test
    void getEventOrganizers() {
        EventDto ed = eventService.add(1L, newEventDto);
        organizerService.add(1L, ed.getId(), 1L, "менеджер");
        organizerService.add(1L, ed.getId(), 2L, "исполнитель");
        organizerService.add(1L, ed.getId(), 3L, "исполнитель");

        OrganizerDto dto1 = new OrganizerDto(1L, ed.getId(), "менеджер");
        OrganizerDto dto2 = new OrganizerDto(2L, ed.getId(), "исполнитель");
        OrganizerDto dto3 = new OrganizerDto(3L, ed.getId(), "исполнитель");
        List<OrganizerDto> organizers =  organizerService.getEventOrganizers(1L);
        assertTrue(!organizers.isEmpty());
        assertThat(3, lessThanOrEqualTo(organizers.size()));
        assertTrue(organizers.contains(dto1));
        assertTrue(organizers.contains(dto2));
        assertTrue(organizers.contains(dto3));
    }
*/
/*






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
                1L,
                null);
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
                1L,
                null);
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
                1L,
                null);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                eventService.delete(1000000000000L,
                        eventQuery.setParameter("name", event.getName()).getSingleResult().getId()));

        assertEquals("Удалить событие может только создатель", exception.getMessage());
    }

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
                1L,
                null);
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
                1L,
                null);
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
                1L,
                null);
        em.persist(event);
        em.flush();

        TypedQuery<Event> eventQuery = em.createQuery("select e from Event as e where e.name = :name", Event.class);

        EventDto eventDto = eventService.getById(100000000L,
                eventQuery.setParameter("name", event.getName()).getSingleResult().getId());

        assertThat(eventDto.getCreatedDateTime(), nullValue());
    }
    */
}