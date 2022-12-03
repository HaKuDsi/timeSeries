package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.TagsModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.EventRepository;
import com.timeseries.seriestemporelles.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {

    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    EventRepository eventRepository;

    public List<TagsModel> getAllTags() {
        return tagsRepository.findAll();
    }

    public List getTagsOfEvent(EventModel event) {
        return tagsRepository.findTagsOfEvent(event);
    }

    public List getTagsOfSerie(SeriesModel serie) {
        List tags = new ArrayList();
        List<EventModel> events = eventRepository.findBySerie(serie);
        for(EventModel event : events) {
            tags.add(getTagsOfEvent(event));
        }
        return tags;
    }

    public void saveOrUpdate(TagsModel tag) { tagsRepository.save(tag); }

    public void delete(TagsModel tag) { tagsRepository.delete(tag); }

    public TagsModel getTagByEventLabel(EventModel event, String label) {
        return tagsRepository.getTagByEventLabel(event, label);
    }

    public List getEventsOfTag(String label) {
        return tagsRepository.findEventsByTag(label);
    }
}
