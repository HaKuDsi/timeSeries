package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.service.EventService;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import com.timeseries.seriestemporelles.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    EventService eventService;

    @InjectMocks
    EventController eventController;

    @Mock
    SeriesService seriesService;


    @Mock
    UserService userService;

    @Mock
    UserSerieService userSerieService;
    ZoneId timeZone = ZoneId.of("UTC");
    ZonedDateTime zonedDateTime = LocalDateTime.parse("2011-12-03T10:15:30").atZone(timeZone);
    SeriesModel serie = new SeriesModel(3, "runnung", "i like to run");

    EventModel event = new EventModel(1, zonedDateTime, 10, "yeah", serie);
    EventModel event2 = new EventModel(2, zonedDateTime, 10, "yeah", serie);

    @Test
    public void getAllEventsTest() {
        var events = new EventModel[]{
                event,
                event2
        };

        when(eventService.getAllEvents()).thenReturn(Arrays.asList(events));

        var response = eventController.getAllEvents();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getEventValue(), f.getComment()))
                .containsExactly(
                        Tuple.tuple(1, 10, "yeah"),
                        Tuple.tuple(2, 10, "yeah")
                );

        verify(eventService).getAllEvents();
    }

    @Test
    public void getEventByIdTest() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.of(event));

        var response = eventController.getEventById(event.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(event);
        verify(eventService).getEventById(event.getId());
    }

    @Test
    public void getEventByIdTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventById(event.getId(), anyInt()));
        assertTrue(exeption.getMessage().contains("User: 0 is not found."));
    }
    
    @Test
    public void getEventByIdTest_noUserSerie() {
        UserModel user = new UserModel(4, "hadi");
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventById(event.getId(), user.getId()));

        assertTrue(exeption.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void getEventByIdTest_noEvent() {

        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(anyInt())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventById(anyInt(), user.getId()));

        assertTrue(exception.getMessage().contains("Event: 0 is not found."));
        verify(eventService).getEventById(anyInt());
    }

    @Test
    public void getEventByIdTest_fail() {
       // var response = eventController.getEventById(null, null);
        //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getEventsOfSerieTest() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);
        var events = new EventModel[]{
                event,
                event2
        };
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.of(serie));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        when(eventService.getEventsOfSerie(serie.getId())).thenReturn(Arrays.asList(events));

        var response = eventController.getEventsOfSerie(user.getId(), serie.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getEventValue(), f.getComment()))
                .containsExactly(
                        Tuple.tuple(1, 10, "yeah"),
                        Tuple.tuple(2, 10, "yeah")
                );
        verify(eventService).getEventsOfSerie(serie.getId());
    }

    @Test
    public void getEventOfSerieTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventsOfSerie(anyInt(), serie.getId()));
        assertTrue(exeption.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void getEventOfSerieTest_noSerie() {
        UserModel user = new UserModel(4, "hadi");
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventsOfSerie(user.getId(), anyInt()));
        assertTrue(exeption.getMessage().contains("Serie: 0 is not found."));
    }

    @Test
    public void getEventOfSerieTest_noUserSerie() {
        UserModel user = new UserModel(4, "hadi");
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.of(serie));
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> eventController.getEventsOfSerie(user.getId(), serie.getId()));
        assertTrue(exeption.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void createEntityTest() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.of(serie));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        doNothing().when(eventService).saveOrUpdate(event);

        var response = eventController.createEntity(user.getId(),
                serie.getId(),
                event,
                "2011-12-03T10:15:30");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(eventService).saveOrUpdate(event);
    }



    @Test
    public void createEventTest_noPrivilage() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.READ_PRIVILAGE, true);

        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.of(serie));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = eventController.createEntity(user.getId(),
                serie.getId(),
                event,
                "2011-12-03T10:15:30");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateEventTest() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        doNothing().when(eventService).saveOrUpdate(event);

        var response = eventController.updateEvent(event.getId(),
                user.getId(),
                "2011-12-03T10:15:30");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(eventService).saveOrUpdate(event);
    }

    @Test
    public void updateEventTest_noPrivilage() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.READ_PRIVILAGE, true);

        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = eventController.updateEvent(event.getId(),
                user.getId(),
                "2011-12-03T10:15:30");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void deleteEventById() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        doNothing().when(eventService).delete(event);

        var response = eventController.deleteEventById(event.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(eventService).delete(event);
    }
    @Test
    public void deleteEventById_noPrivilage() {
        UserModel user = new UserModel(4, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.READ_PRIVILAGE, true);

        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = eventController.deleteEventById(event.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

}
