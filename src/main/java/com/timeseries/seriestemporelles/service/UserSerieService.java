package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.repository.UserSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSerieService {

    @Autowired
    UserSerieRepository userSerieRepository;

    public List getAllUserSeries() {
        List userSeries = new ArrayList();
        userSerieRepository.findAll().forEach(userSerie -> userSeries.add(userSerie));
        return userSeries;
    }

    public UserSeriesModel getUserSerieById(int id) { return userSerieRepository.findById(id).get(); }

    public void saveOrUpdate(UserSeriesModel userSeries) { userSerieRepository.save(userSeries); }

    public void delet(int id) { userSerieRepository.deleteById(id); }
}
