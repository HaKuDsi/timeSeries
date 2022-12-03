package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSerieRepository extends JpaRepository <UserSeriesModel, Integer> {
    @Query(value = "select u_s from UserSeriesModel u_s " +
            "where u_s.users = ?1 and " +
            "u_s.series = ?2")
    Optional<UserSeriesModel> userSerieExist(UserModel user, SeriesModel serie);

    @Query(value = "select userSerie from UserSeriesModel userSerie " +
            "where userSerie.series = ?1")
    List<UserSeriesModel> allUserSeriesFromSerie(SeriesModel series);
}
