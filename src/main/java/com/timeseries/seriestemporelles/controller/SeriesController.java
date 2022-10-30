package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class SeriesController {

    @Autowired
    SeriesService seriesService;
    @Autowired
    UserSerieService userSerieService;

    @GetMapping("/series")
    private List getAllSeries() { return seriesService.getAllSeries(); }

    @GetMapping("/serie/{id}")
    private SeriesModel getSerieById(@PathVariable("id") int id) { return seriesService.getSerieById(id); }

    @PostMapping("/series")
    private ResponseEntity createSerie(@RequestBody SeriesModel series) {
        try {
            series.setLastUpdatedDate();
            //seriesService.saveOrUpdate(series);

            UserSeriesModel newUserSeries = new UserSeriesModel();
            userSerieService.saveOrUpdate(newUserSeries);

            //newUserSeries.setSeries(series);
            //userSerieService.saveOrUpdate(newUserSeries);

            series.setUserSeries(newUserSeries);
            seriesService.saveOrUpdate(series);

        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
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
