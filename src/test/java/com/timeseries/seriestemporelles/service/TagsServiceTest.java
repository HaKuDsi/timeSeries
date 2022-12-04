package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.repository.EventRepository;
import com.timeseries.seriestemporelles.repository.TagsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagsServiceTest {

    @Mock
    TagsRepository tagsRepository;

    @InjectMocks
    TagsService tagsService;

    @Mock
    EventRepository eventRepository;

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
    public void getAllTagsTest() {
        var tags = new TagsModel[]{
                tag1,
                tag2,
                tag3,
                tag4
        };
        when(tagsRepository.findAll()).thenReturn(Arrays.asList(tags));

        var response = tagsService.getAllTags();

        assertThat(response).isNotNull();
        assertThat(response).contains(tag4);
    }

    @Test
    public void getTagsOfEventTest() {
        var tags = new TagsModel[]{
                tag1,
                tag2
        };
        when(tagsRepository.findTagsOfEvent(event)).thenReturn(Arrays.asList(tags));

        var response = tagsService.getTagsOfEvent(event);

        assertThat(response).isNotNull();
        assertThat(response).contains(tag2);
    }

    @Test
    public void getTagsOfSerieTest() {
        var allTags = new TagsModel[]{
                tag1,
                tag2,
                tag3,
                tag4
        };

        var tags1 = new TagsModel[]{
                tag1,
                tag2
        };

        var tags2 = new TagsModel[]{
                tag3,
                tag4
        };

        var events = new EventModel[]{
                event,
                event2
        };

        when(eventRepository.findBySerie(serie)).thenReturn(Arrays.asList(events));
        when(tagsService.getTagsOfEvent(event)).thenReturn(Arrays.asList(tags1));
        when(tagsService.getTagsOfEvent(event2)).thenReturn(Arrays.asList(tags2));

        var response = tagsService.getTagsOfSerie(serie);

        assertThat(response).isNotNull();
        assertThat(response).contains(allTags);
        assertThat(response).contains(tag3);
    }

    @Test
    public void saveOrUpdateTag() {
        tagsService.saveOrUpdateTag(tag1);

        verify(tagsRepository).save(tag1);
    }

    @Test
    public void deleteTagTest() {
        tagsService.deleteTag(tag1);

        verify(tagsRepository).delete(tag1);
    }

    @Test
    public void getTagByEventLabelTest() {
        when(tagsRepository.getTagByEventLabel(event, tag1.getLabel())).thenReturn(tag1);

        var response = tagsService.getTagByEventLabel(event, tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(tag1);
    }

    @Test
    public void getEventsOfTagTest() {
        var events = new EventModel[]{
                event,
                event2
        };

        when(tagsRepository.findEventsByTag(tag1.getLabel())).thenReturn(Arrays.asList(events));

        var response = tagsService.getEventsOfTag(tag1.getLabel());

        assertThat(response).isNotNull();
        assertThat(response).contains(event);
    }
}
