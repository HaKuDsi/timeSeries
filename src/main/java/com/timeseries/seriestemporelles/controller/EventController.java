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
import org.springframework.util.Assert;
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
    public ResponseEntity<List<EventModel>> getAllEvents() { return ResponseEntity.ok(eventService.getAllEvents());}

    @GetMapping("/event/{id}/user_id={user_id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable("id") Integer id,
                                    @PathVariable("user_id") Integer userId) {
        Assert.notNull(id, "cannot fetch null id");
        Assert.notNull(userId, "cannot fetch null id");
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        SeriesModel serie = eventService.getSerieByEvent(id);

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        EventModel eventModel = eventService.getEventById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event: " + id + " is not found."));

        return ResponseEntity.ok(eventModel);
    }

    @GetMapping("/event/user_id={user_id}/serie_id={serie_id}")
    public ResponseEntity<List<EventModel>> getEventsOfSerie(@PathVariable("user_id") Integer userId,
                                  @PathVariable("serie_id") Integer serieId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        SeriesModel serie = seriesService.getSerieById(serieId).orElseThrow(() ->
                new ResourceNotFoundException("Serie: " + serieId + " is not found."));

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        return ResponseEntity.ok(eventService.getEventsOfSerie(serieId));
    }

    @PostMapping("/event/user_id={user_id}/serie_id={serie_id}")
    public ResponseEntity createEntity(@PathVariable("user_id") Integer userId,
                                        @PathVariable("serie_id") Integer serieId ,
                                        @RequestBody EventModel event,
                                        @RequestParam String eventDate) {
        try {
            Assert.hasText(eventDate, "must add date");
            Assert.notNull(event.getEventValue(), "must add value");
            Assert.notNull(event.getSerie(), "Must add series");
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
                eventService.saveOrUpdateEvent(event);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("New event created with id : " + event.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/event/{id}/user_id={user_id}")
    public ResponseEntity updateEvent(@PathVariable("id") Integer id,
                                        @PathVariable("user_id") Integer userId,
                                        @RequestParam String eventDate) {

        try {
            Assert.hasText(eventDate, "must add date");
            EventModel event = eventService.getEventById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Event: " + id + " is not found."));

            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + " is not found."));

            SeriesModel serie = eventService.getSerieByEvent(id);

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if (userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
                event.setLastUpdatedDate();
                event.setEventDate(eventDate);
                eventService.saveOrUpdateEvent(event);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("Event with id: " + id + " is modified", HttpStatus.OK);
    }

    @DeleteMapping("/event/{id}/user_id={user_id}")
    public ResponseEntity deleteEventById(@PathVariable("id") Integer id,
                                      @PathVariable("user_id") Integer userId) {
        try {
            Assert.notNull(id, "id cannot be null");
            EventModel event = eventService.getEventById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Event: " + id + " is not found."));

            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + " is not found."));

            SeriesModel serie = eventService.getSerieByEvent(id);

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));
            
            if(userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
                eventService.deleteEventById(event.getId());
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("Event delete with id: " + id, HttpStatus.OK);
    }
}