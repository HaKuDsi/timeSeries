package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "TBL_SERIES")
public class SeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private String lastUpdatedDate;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLastUpdatedDate() {
        //String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.lastUpdatedDate);
        return this.lastUpdatedDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdatedDate() {
        Clock clock = Clock.systemUTC();
        this.lastUpdatedDate = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(LocalDateTime.now());
    }

    public SeriesModel() {
    }
}
