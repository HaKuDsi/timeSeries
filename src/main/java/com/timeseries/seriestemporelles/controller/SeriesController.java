package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserPrivilage;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import com.timeseries.seriestemporelles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    SeriesService seriesService;
    @Autowired
    UserSerieService userSerieService;
    @Autowired
    UserService userService;

    @GetMapping("/series")
    public List getAllSeries() { return seriesService.getAllSeries(); }

    @GetMapping("/serie/{id}/user_id={user_id}")
    public SeriesModel getSerieById(@PathVariable("id") Integer id,
                                     @PathVariable("user_id") Integer userId) {
        UserModel user = userService.getUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User: " + userId + "not found."));

        SeriesModel serie = seriesService.getSerieById(id).orElseThrow(() ->
                new ResourceNotFoundException("Serie: " + id + " is not found."));

        UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                new ResourceNotFoundException("UserSerie is not found."));

        if(userSerie.getUserPrivilege() == UserPrivilage.WRITE_PRIVILAGE) {
            return serie;
        }
        return null;
    }

    @PostMapping("/serie/{id}")
    public ResponseEntity createSerie(@PathVariable("id") Integer id,
                                       @RequestBody SeriesModel series) {
        try {
            series.setLastUpdatedDate();
            seriesService.saveOrUpdate(series);

            UserModel user = userService.getUserById(id).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + id + " is not found."));

            UserSeriesModel userSeries = new UserSeriesModel();
            userSeries.setUsers(user);
            userSeries.setSeries(series);
            userSeries.setOwner(true);
            userSeries.setUserPrivilege(UserPrivilage.WRITE_PRIVILAGE);
            userSerieService.saveOrUpdate(userSeries);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("New series created with id: " + series.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/serie/{id}/user_id={user_id}")
    public ResponseEntity updateSerie(@PathVariable("id") Integer id,
                                       @PathVariable("user_id") Integer userId,
                                       @RequestBody SeriesModel series) {
        try {
            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + "not found."));

            SeriesModel serie = seriesService.getSerieById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Serie: " + id + " is not found."));

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if(userSerie.getOwner()) {
                seriesService.saveOrUpdate(series);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity("New series created with id: " + series.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/serie/{id}/user_id={user_id}")
    public ResponseEntity deleteById(@PathVariable("id") Integer id,
                                      @PathVariable("user_id") Integer userId) {
        try {
            UserModel user = userService.getUserById(userId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + userId + "not found."));

            SeriesModel serie = seriesService.getSerieById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Serie: " + id + " is not found."));

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(user, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));

            if(userSerie.getOwner()) {
                seriesService.delete(id);
                userSerieService.delete(userSerie.getId());
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Serie delete with id: " + id, HttpStatus.OK);
    }
}
