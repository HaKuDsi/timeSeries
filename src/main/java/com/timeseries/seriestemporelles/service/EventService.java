package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.repository.EventRepository;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SeriesRepository seriesRepository;

    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<EventModel> getEventsOfSerie(int id) {
        SeriesModel serie = seriesRepository.findById(id).get();
        return eventRepository.findBySerie(serie);
    }

    @Cacheable("event")
    public Optional<EventModel> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public void saveOrUpdateEvent(EventModel event) {
        Assert.notNull(event.getEventValue(), "event must have a value.");
        Assert.notNull(event.getSerie(), "event must belong to a serie");
        Assert.notNull(event.getEventDate(), "event must have a date");
        eventRepository.save(event);
    }

    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);
    }

    public SeriesModel getSerieByEvent(Integer id) {
        return eventRepository.findSerieByEvent(id);
    }
}
