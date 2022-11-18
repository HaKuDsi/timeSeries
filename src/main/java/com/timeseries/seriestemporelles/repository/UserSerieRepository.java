package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.UserSeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSerieRepository extends JpaRepository <UserSeriesModel, Integer> {
}
