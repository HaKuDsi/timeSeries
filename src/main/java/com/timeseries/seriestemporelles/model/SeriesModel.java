package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

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
    private ZonedDateTime lastUpdatedDate;

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
        String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.lastUpdatedDate);
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public SeriesModel() {
    }

    @ManyToOne
    @JoinColumn(name = "USER_SERIES_ID")
    private UserSeriesModel userSeriesModel;

    public UserSeriesModel getUserSeriesModel() {
        return userSeriesModel;
    }
}
