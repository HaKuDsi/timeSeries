package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.service.EventService;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import com.timeseries.seriestemporelles.service.UserService;
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

    @Autowired
    UserService userService;

    @Autowired
    UserSerieService userSerieService;

    @GetMapping("/events")
    private List getAllEvents() { return eventService.getAllEvents();}

    @GetMapping("/event/{id}/user_id={user_id}")
    private EventModel getEventById(@PathVariable("id") Integer id,
                                    @PathVariable("user_id") Integer userId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        SeriesModel serie = eventService.getSerieByEvent(id);

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        return eventService.getEventById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event: " + id + " is not found."));
    }

    @GetMapping("/event/user_id={user_id}/serie_id={serie_id}")
    private List getEventsOfSerie(@PathVariable("user_id") Integer userId,
                                  @PathVariable("serie_id") Integer serieId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        SeriesModel serie = seriesService.getSerieById(serieId).orElseThrow(() ->
                new ResourceNotFoundException("Serie: " + serieId + " is not found."));

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        return eventService.getEventsOfSerie(serieId);
    }

    @PostMapping("/event/user_id={user_id}/serie_id={serie_id}")
    private ResponseEntity createEntity(@PathVariable("user_id") Integer userId,
                                        @PathVariable("serie_id") Integer serieId ,
                                        @RequestBody EventModel event,
                                        @RequestParam String eventDate) {
        try {
            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + " is not found."));

            SeriesModel serie = seriesService.getSerieById(serieId).orElseThrow(() ->
                    new ResourceNotFoundException("Serie: " + serieId + " is not found."));

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if(userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
                event.setLastUpdatedDate();
                event.setSerie(serie);
                event.setEventDate(eventDate);
                eventService.saveOrUpdate(event);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("New event created with id : " + event.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/event/{id}")
    private ResponseEntity updateEntity(@PathVariable("id") Integer id,
                                        @RequestParam String eventDate) {
        EventModel event = eventService.getEventById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event: " + id + " is not found."));
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
            EventModel event = eventService.getEventById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Event: " + id + " is not found."));
            eventService.delete(event);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("Event delete with id: " + id, HttpStatus.OK);
    }
}