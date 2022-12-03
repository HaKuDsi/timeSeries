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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserSerieController {

    @Autowired
    UserSerieService userSerieService;

    @Autowired
    SeriesService seriesService;

    @Autowired
    UserService userService;

    @GetMapping("/userseries")
    public ResponseEntity<List<UserSeriesModel>> getAllUserSeries() { return ResponseEntity.ok(userSerieService.getAllUserSeries()); }

    @PostMapping("userseries/serie_id={serie_id}/from={from_id}/to_id={to_id}")
    public ResponseEntity shareSerie(@PathVariable("serie_id") Integer serieId,
                                     @PathVariable("from_id") Integer fromId,
                                     @PathVariable("to_id") Integer toId,
                                     @RequestParam Boolean write) {
        try {
            Assert.notNull(write, "serie id cannot be null");
            UserModel fromUser = userService.getUserById(fromId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + fromId + " is not found."));

            UserModel toUser = userService.getUserById(toId).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + toId + " is not found."));

            SeriesModel serie = seriesService.getSerieById(serieId).orElseThrow(() ->
                    new ResourceNotFoundException("Serie: " + serieId + " is not found."));

            UserSeriesModel userSerie = userSerieService.getUserSerieByUserSerie(fromUser, serie).orElseThrow(() ->
                    new ResourceNotFoundException("UserSerie is not found."));
            if(userSerie.getOwner()) {
                UserSeriesModel shared = new UserSeriesModel();
                shared.setSeries(serie);
                shared.setUsers(toUser);
                shared.setOwner(false);
                if(write) {
                    shared.setUserPrivilege(UserPrivilage.WRITE_PRIVILAGE);
                } else {
                    shared.setUserPrivilege(UserPrivilage.READ_PRIVILAGE);
                }
                userSerieService.saveOrUpdate(shared);
            } else {
                return new ResponseEntity("User doesn't have permission", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity("New event created with id : " + userSerie.getId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
