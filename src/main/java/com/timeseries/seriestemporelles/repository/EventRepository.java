package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.SeriesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository <EventModel, Integer> {
    @Query(value = "select events from EventModel events where events.serie = ?1")
    List<EventModel> findBySerie(SeriesModel serieId);

    @Query(value = "select events.serie from EventModel events where events.id = ?1")
    SeriesModel findSerieByEvent(Integer event);
}
