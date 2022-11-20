package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.service.EventService;
import com.timeseries.seriestemporelles.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    @PostMapping("/event/{id}")
    private ResponseEntity createEntity(@PathVariable("id") Integer id, @RequestParam String eventDate) {
        try {
            EventModel event = new EventModel();
            event.setLastUpdatedDate();
            SeriesModel serie = seriesService.getSerieById(id);
            event.setSerie(serie);

            event.setEventDate(eventDate);

            eventService.saveOrUpdate(event);
        } catch (Exception exception) {
            return new ResponseEntity((HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return new ResponseEntity("New event created with id : ", HttpStatus.CREATED);
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