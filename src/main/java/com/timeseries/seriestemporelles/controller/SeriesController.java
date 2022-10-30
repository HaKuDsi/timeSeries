package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @GetMapping("/series")
    private List getAllSeries() { return seriesService.getAllSeries(); }

    @GetMapping("/serie/{id}")
    private SeriesModel getSerieById(@PathVariable("id") int id) { return seriesService.getSerieById(id); }

    @PostMapping("/series")
    private ResponseEntity createSerie(@RequestBody SeriesModel serie) {
        try {
            serie.setLastUpdatedDate();
            seriesService.saveOrUpdate(serie);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("New serie created with id: " + serie.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/series.{id}")
    private ResponseEntity deleteById(@PathVariable("id") int id) {
        try {
            seriesService.delete(id);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Serie delete with id: " + id, HttpStatus.OK);
    }
}
