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
    
}
