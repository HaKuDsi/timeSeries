package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "TBL_SERIES")
public class SeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SERIE_ID")
    private Integer id;

    private String title;

    private String description;

    private ZonedDateTime lastUpdatedDate;

    public SeriesModel() {
    }

    public SeriesModel(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        setLastUpdatedDate();
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdatedDate() {
        String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.lastUpdatedDate);
        return date;
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SeriesModel other = (SeriesModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
