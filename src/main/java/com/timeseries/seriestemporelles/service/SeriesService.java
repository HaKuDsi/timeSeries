package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    @Autowired
    SeriesRepository seriesRepository;

    public List<SeriesModel> getAllSeries() {
        return seriesRepository.findAll();
    }

    public Optional<SeriesModel> getSerieById(int id) { return seriesRepository.findById(id); }

    public void saveOrUpdate(SeriesModel serie) { seriesRepository.save(serie); }

    public void delete(int id) { seriesRepository.deleteById(id); }
}
