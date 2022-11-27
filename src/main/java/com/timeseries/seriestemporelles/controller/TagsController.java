package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagsController {

    @Autowired
    EventService eventService;

    @Autowired
    SeriesService seriesService;

    @Autowired
    UserService userService;

    @Autowired
    UserSerieService userSerieService;

    @Autowired
    TagsService tagsService;

    @GetMapping("/tags/event_id={event_id}/user_id={user_id}")
    private List getTagsOfEvent(@PathVariable("event_id") Integer eventId,
                                @PathVariable("user_id") Integer userId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        EventModel event = eventService.getEventById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Event: " + eventId + " is not found."));

        SeriesModel serie = eventService.getSerieByEvent(eventId);

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        return tagsService.getTagsOfEvent(event);
    }

    @GetMapping("/tags/serie_id={serie_id}/user_id={user_id}")
    private List getTagsOfSerie(@PathVariable("serie_id") Integer serieId,
                                @PathVariable("user_id") Integer userId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + " is not found."));

        SeriesModel serie = seriesService.getSerieById(serieId).orElseThrow(() ->
                new ResourceNotFoundException("Serie: " + serieId + "not found."));

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        if(userSerie.getOwner()) {
            return tagsService.getTagsOfSerie(serie);
        }
        return null;
    }

    @PostMapping("/tags/event_id={event_id}/user_id={user_id}")
    private ResponseEntity createTagToEvent(@PathVariable("event_id") Integer eventId,
                                            @PathVariable("user_id") Integer userId,
                                            @RequestParam String label) {
        try {
            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + " is not found."));

            EventModel event = eventService.getEventById(eventId).orElseThrow(() ->
                    new ResourceNotFoundException("Event: " + eventId + " is not found."));

            SeriesModel serie = eventService.getSerieByEvent(eventId);

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if(userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
                TagsModel tag = new TagsModel();
                tag.setLabel(label);
                tag.setEvent(event);
                tag.setLastUpdatedDate();
                tagsService.saveOrUpdate(tag);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("New tag created with id", HttpStatus.CREATED);
    }

    @DeleteMapping("/tags/event_id={event_id}/user_id={user_id}")
    private ResponseEntity deleteTag(@PathVariable("event_id") Integer eventId,
                                            @PathVariable("user_id") Integer userId,
                                            @RequestParam String label) {
        try {
            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + " is not found."));

            EventModel event = eventService.getEventById(eventId).orElseThrow(() ->
                    new ResourceNotFoundException("Event: " + eventId + " is not found."));

            SeriesModel serie = eventService.getSerieByEvent(eventId);

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if(userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
                TagsModel tag = tagsService.getTagByEventLabel(event, label);
                tagsService.delete(tag);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("tag deleted", HttpStatus.OK);
    }
}
