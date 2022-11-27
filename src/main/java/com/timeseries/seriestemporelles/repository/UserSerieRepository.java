package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSerieRepository extends JpaRepository <UserSeriesModel, Integer> {
    @Query(value = "select u_s from UserSeriesModel u_s " +
            "where u_s.users = ?1 and " +
            "u_s.series = ?2")
    UserSeriesModel userSerieExist(UserModel user, SeriesModel serie);
}
