package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.repository.EventRepository;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SeriesRepository seriesRepository;

    public List getAllEvents() {
        List events = new ArrayList();
        eventRepository.findAll().forEach(event -> events.add(event));
        return events;
    }

    public List getEventsOfSerie(int id) {
        List events = new ArrayList();
        SeriesModel serie = seriesRepository.findById(id).get();
        return (List) eventRepository.findBySerie(serie);
    }

    public Optional<EventModel> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public void saveOrUpdate(EventModel event) {
        eventRepository.save(event);
    }

    public void delete(EventModel event) {
        eventRepository.delete(event);
    }
}
