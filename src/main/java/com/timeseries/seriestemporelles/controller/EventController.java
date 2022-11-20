package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.service.EventService;
import com.timeseries.seriestemporelles.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    SeriesService seriesService;

    @GetMapping("/events")
    private List getAllEvents() { return eventService.getAllEvents();}

    @GetMapping("/event/{id}")
    private EventModel getEventById(@PathVariable("id") Integer id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/events/{id}")
    private List getEventsOfSerie(@PathVariable("id") Integer id) {
        return eventService.getEventsOfSerie(id);
    }

    @PostMapping("/event/serie_id={id}")
    private ResponseEntity createEntity(@PathVariable("id") Integer id, @RequestBody EventModel event, @RequestParam String eventDate) {
        try {
            //EventModel event = new EventModel();
            event.setLastUpdatedDate();
            SeriesModel serie = seriesService.getSerieById(id);
            event.setSerie(serie);

            event.setEventDate(eventDate);
            eventService.saveOrUpdate(event);

        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("New event created with id : " + event.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/event/{id}")
    private ResponseEntity updateEntity(@PathVariable("id") Integer id, @RequestParam String eventDate) {
        EventModel event = eventService.getEventById(id);
        if(event != null) {
            event.setEventDate(eventDate);
            eventService.saveOrUpdate(event);
            return new ResponseEntity("Event with id: " + id + " is modified", HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/event/{id}")
    private ResponseEntity deleteById(@PathVariable("id") Integer id) {
        try {
            eventService.delete(eventService.getEventById(id));
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Event delete with id: " + id, HttpStatus.OK);
    }
}