package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.SeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository <SeriesModel, Integer> {
}
