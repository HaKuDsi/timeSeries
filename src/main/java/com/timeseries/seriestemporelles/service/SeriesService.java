package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    @Autowired
    SeriesRepository seriesRepository;

    public List<SeriesModel> getAllSeries() {
        return seriesRepository.findAll();
    }

    @Cacheable("serie")
    public Optional<SeriesModel> getSerieById(int id) { return seriesRepository.findById(id); }

    public void saveOrUpdateSerie(SeriesModel serie) {
        Assert.notNull(serie.getTitle(),"title cannot be empty");
        Assert.notNull(serie.getDescription(), "description cannot be null");
        seriesRepository.save(serie);
    }

    public void deleteSerieById(Integer id) { seriesRepository.deleteById(id); }
}
