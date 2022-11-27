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

import java.util.Collections;
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
    private List getAllSeries() { return seriesService.getAllSeries(); }

    @GetMapping("/serie/{id}")
    private SeriesModel getSerieById(@PathVariable("id") Integer id) {
        return seriesService.getSerieById(id).orElseThrow(() ->
            new ResourceNotFoundException("Serie: " + id + " is not found."));
    }

    @PostMapping("/serie/{id}")
    private ResponseEntity createSerie(@PathVariable("id") Integer id, @RequestBody SeriesModel series) {
        try {
            series.setLastUpdatedDate();
            seriesService.saveOrUpdate(series);

            UserModel user = userService.getUserById(id).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + id + "is not found."));

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

    @DeleteMapping("/serie/{id}")
    private ResponseEntity deleteById(@PathVariable("id") int id) {
        try {
            seriesService.delete(id);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Serie delete with id: " + id, HttpStatus.OK);
    }
}
