package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.repository.UserSerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSerieService {

    @Autowired
    UserSerieRepository userSerieRepository;

    public List<UserSeriesModel> getAllUserSeries() {
        return userSerieRepository.findAll();
    }
    @Cacheable("userSerie")
    public Optional<UserSeriesModel> getUserSerieById(int id) { return userSerieRepository.findById(id); }

    public void saveOrUpdate(UserSeriesModel userSeries) { userSerieRepository.save(userSeries); }

    public void delete(int id) { userSerieRepository.deleteById(id); }

    @Cacheable("userSerie")
    public Optional<UserSeriesModel> getUserSerieByUserSerie(UserModel user, SeriesModel serie) {
        return userSerieRepository.userSerieExist(user, serie);
    }

    public List<UserSeriesModel> getAllUserSeriesBySerie(SeriesModel serie) {
        return userSerieRepository.allUserSeriesFromSerie(serie);
    }
}
