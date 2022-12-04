package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.EventRepository;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventService eventService;

    @Mock
    SeriesRepository seriesRepository;

    ZoneId timeZone = ZoneId.of("UTC");
    ZonedDateTime zonedDateTime = LocalDateTime.parse("2011-12-03T10:15:30").atZone(timeZone);
    SeriesModel serie = new SeriesModel(3, "runnung", "i like to run");

    EventModel event = new EventModel(1, zonedDateTime, 10, "yeah", serie);
    EventModel event2 = new EventModel(2, zonedDateTime, 10, "yeah", serie);
    UserModel user = new UserModel(4, "hadi");

    @Test
    public void getAllEventsTest() {
        var events = new EventModel[]{
                event,
                event2
        };
        when(eventRepository.findAll()).thenReturn(Arrays.asList(events));

        var response = eventService.getAllEvents();

        assertThat(response).isNotNull();
        assertThat(response).contains(event);
    }

    @Test
    public void getEventsOfSerieTest() {
        var events = new EventModel[]{
                event,
                event2
        };
        when(seriesRepository.findById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(eventRepository.findBySerie(serie)).thenReturn(Arrays.asList(events));

        var response = eventService.getEventsOfSerie(serie.getId());

        assertThat(response).isNotNull();
        assertThat(response).contains(event);
    }

    @Test
    public void getEventByIdTest() {
        when(eventRepository.findById(event.getId())).thenReturn(Optional.ofNullable(event));

        var response = eventService.getEventById(event.getId());

        assertThat(response).isNotNull();
        assertThat(response).contains(event);
    }

    @Test
    public void saveOrUpdateEventTest() {
        eventService.saveOrUpdateEvent(event);

        verify(eventRepository).save(event);
    }

    @Test
    public void deleteEventById() {
        eventService.deleteEventById(event.getId());

        verify(eventRepository).deleteById(event.getId());
    }

    @Test
    public void getSeriesByEventTest() {
        when(eventRepository.findSerieByEvent(event.getId())).thenReturn(serie);

        var response = eventService.getSerieByEvent(event.getId());

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(serie);
    }
}
