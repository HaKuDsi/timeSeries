package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.TagsModel;
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

    public List<TagsModel> getTagsOfEvent(EventModel event) {
        return tagsRepository.findTagsOfEvent(event);
    }

    public List<TagsModel> getTagsOfSerie(SeriesModel serie) {
        List tags = new ArrayList();
        List<EventModel> events = eventRepository.findBySerie(serie);
        for(EventModel event : events) {
            List<TagsModel> thisTags = getTagsOfEvent(event);
            for(TagsModel t : thisTags) {
                tags.add(t);
            }
        }
        return tags;
    }

    public void saveOrUpdateTag(TagsModel tag) { tagsRepository.save(tag); }

    public void deleteTag(TagsModel tag) { tagsRepository.delete(tag); }

    public TagsModel getTagByEventLabel(EventModel event, String label) {
        return tagsRepository.getTagByEventLabel(event, label);
    }

    public List<EventModel> getEventsOfTag(String label) {
        return tagsRepository.findEventsByTag(label);
    }
}
