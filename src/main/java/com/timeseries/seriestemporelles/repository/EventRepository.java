package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository <EventModel, Integer> {
}
