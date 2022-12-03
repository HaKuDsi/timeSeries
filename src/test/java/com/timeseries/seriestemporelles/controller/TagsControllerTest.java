package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.service.*;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagsControllerTest {
    @Mock
    TagsService tagsService;

    @InjectMocks
    TagsController tagsController;

    @Mock
    EventService eventService;

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
    UserModel user = new UserModel(4, "hadi");

    TagsModel tag1 = new TagsModel(5, "RUN", event);
    TagsModel tag2 = new TagsModel(6, "100km", event);

    TagsModel tag3 = new TagsModel(7, "RUN", event2);
    TagsModel tag4 = new TagsModel(8, "20m", event2);

    @Test
    public void getTagsOfEventTest() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);
        var tags = new TagsModel[]{
                tag1,
                tag2
        };
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user,serie)).thenReturn(Optional.of(userSeries));
        when(tagsService.getTagsOfEvent(event)).thenReturn(Arrays.asList(tags));

        var response = tagsController.getTagsOfEvent(event.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getLabel(), f.getEvent()))
                .containsExactly(
                        Tuple.tuple(tag1.getId(), tag1.getLabel(), tag1.getEvent()),
                        Tuple.tuple(tag2.getId(), tag2.getLabel(), tag2.getEvent())
                );
        verify(tagsService).getTagsOfEvent(event);
    }

    @Test
    public void getTagsOfEventTest_noUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfEvent(event.getId(), anyInt()));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void getTagsOfEventTest_noEvent() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfEvent(anyInt(), user.getId()));
        assertTrue(exception.getMessage().contains("Event: 0 is not found."));
    }

    @Test
    public void getTagsOfEventTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfEvent(event.getId(), user.getId()));
        assertTrue(exception.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void getTagsOfSerieTest() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);
        var tags = new TagsModel[]{
                tag1,
                tag2
        };

        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userSerieService.getUserSerieByUserSerie(user,serie)).thenReturn(Optional.of(userSeries));
        when(tagsService.getTagsOfSerie(serie)).thenReturn(Arrays.asList(tags));
        var response = tagsController.getTagsOfSerie(serie.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getLabel(), f.getEvent()))
                .containsExactly(
                        Tuple.tuple(tag1.getId(), tag1.getLabel(), tag1.getEvent()),
                        Tuple.tuple(tag2.getId(), tag2.getLabel(), tag2.getEvent())
                );
        verify(tagsService).getTagsOfSerie(serie);
    }

    @Test
    public void getTagsOfSerieTest_noUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfSerie(serie.getId(), anyInt()));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void getTagsOfSerieTest_noSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfSerie(anyInt(), user.getId()));
        assertTrue(exception.getMessage().contains("Serie: 0 not found."));
    }

    @Test
    public void getTagsOfSerieTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.getTagsOfSerie(serie.getId(), user.getId()));
        assertTrue(exception.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void getEventsOfTagTest() {
        when(tagsService.getEventsOfTag(tag1.getLabel())).thenReturn(Arrays.asList(event, event2));

        var response = tagsController.getEventsOfTag(tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getEventValue(), f.getEventDate()))
                .containsExactly(
                        Tuple.tuple(event.getId(), event.getEventValue(), event.getEventDate()),
                        Tuple.tuple(event2.getId(), event2.getEventValue(), event2.getEventDate())
                );
        verify(tagsService).getEventsOfTag(tag1.getLabel());
    }

    @Test
    public void createTagToEventTest() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = tagsController.createTagToEvent(event.getId(), user.getId(), tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void createTagToEventTest_noUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.createTagToEvent(event.getId(), anyInt(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void createTagToEventTest_noEvent() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.createTagToEvent(anyInt(), user.getId(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("Event: 0 is not found."));
    }

    @Test
    public void createTagToEventTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.createTagToEvent(event.getId(), user.getId(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void createTagToEventTest_noPrivilage() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.READ_PRIVILAGE, false);
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = tagsController.createTagToEvent(event.getId(), user.getId(), tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertEquals(response.getBody().toString(), "User doesn't have permission");
    }

    @Test
    public void createTagToEventTest_fail() {
        var response = tagsController.createTagToEvent(event.getId(), user.getId(), "");
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteTagTest() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));
        when(tagsService.getTagByEventLabel(event, tag1.getLabel())).thenReturn(tag1);
        doNothing().when(tagsService).delete(tag1);

        var response = tagsController.deleteTag(event.getId(), user.getId(), tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString().contains("tag deleted"));
        verify(tagsService).delete(tag1);
    }
    @Test
    public void deleteTagTest_noUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.deleteTag(event.getId(), anyInt(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }
    @Test
    public void deleteTagTest_noEvent() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.deleteTag(anyInt(), user.getId(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("Event: 0 is not found."));
    }
    @Test
    public void deleteTagTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tagsController.deleteTag(event.getId(), user.getId(), tag1.getLabel()));
        assertTrue(exception.getMessage().contains("UserSerie is not found."));
    }
    @Test
    public void deleteTagTest_noPrivilage() {
        UserSeriesModel userSeries = new UserSeriesModel(9, user, serie, UserPrivilage.READ_PRIVILAGE, false);
        when(userService.getUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(eventService.getEventById(event.getId())).thenReturn(Optional.ofNullable(event));
        when(eventService.getSerieByEvent(event.getId())).thenReturn(serie);
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = tagsController.deleteTag(event.getId(), user.getId(), tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString().contains("User doesn't have permission"));
    }
    @Test
    public void deleteTagTest_fail() {
        var response = tagsController.deleteTag(event.getId(), user.getId(), "");
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
