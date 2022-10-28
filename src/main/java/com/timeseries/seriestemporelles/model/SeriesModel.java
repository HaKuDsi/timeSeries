package com.timeseries.seriestemporelles.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return lastUpdatedDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdatedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.lastUpdatedDate = dateFormat.format(date);
    }

    public SeriesModel() {
    }
}
