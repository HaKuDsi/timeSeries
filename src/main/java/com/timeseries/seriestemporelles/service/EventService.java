package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List getAllEvents() {
        List events = new ArrayList();
        eventRepository.findAll().forEach(event -> events.add(event));
        return events;
    }

    public EventModel getEventById(int id) {
        return eventRepository.findById(id).get();
    }

    public void saveOrUpdate(EventModel event) {
        eventRepository.save(event);
    }

    public void delete(EventModel event) {
        eventRepository.delete(event);
    }
}
