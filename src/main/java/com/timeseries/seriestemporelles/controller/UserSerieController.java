package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserSerieController {

    @Autowired
    UserSerieService userSerieService;

    @Autowired
    SeriesService seriesService;

    @GetMapping("/userseries")
    private List getAllUserSeries() { return userSerieService.getAllUserSeries(); }

    @GetMapping("/userserie/{id}")
    private UserSeriesModel getUserSerieById(@PathVariable("id") int id) { return userSerieService.getUserSerieById(id); }

    @PostMapping("/userseries")
    private ResponseEntity createUserSerie(@RequestBody UserSeriesModel userSeries) {
        try {
            SeriesModel serie = new SeriesModel();
            seriesService.saveOrUpdate(serie);

            userSeries.setSeries(serie);
            userSerieService.saveOrUpdate((userSeries));
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("New series created with id: " + userSeries.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/userserie/{id}")
    private ResponseEntity deleteById(@PathVariable("id") int id) {
        try {
            userSerieService.delet(id);
        } catch(Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Serie delete with id: " + id, HttpStatus.OK);
    }
}
