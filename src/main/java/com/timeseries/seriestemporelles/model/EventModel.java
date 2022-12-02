package com.timeseries.seriestemporelles.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "TBL_EVENT")
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID")
    private Integer id;

    private ZonedDateTime eventDate;

    private Integer eventValue;

    private String comment;

    private ZonedDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "SERIE_ID")
    private SeriesModel serie;

    public EventModel() {
    }

    public EventModel(Integer id,
                      ZonedDateTime eventDate,
                      Integer eventValue,
                      String comment,
                      SeriesModel serie) {
        this.id = id;
        this.eventDate = eventDate;
        this.eventValue = eventValue;
        this.comment = comment;
        this.serie = serie;
        setLastUpdatedDate();
    }

    public Integer getId() {
        return id;
    }

    public String getEventDate() {
        String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.eventDate);
        return date;    }

    public void setEventDate(String eventDateString) {

        ZoneId zone = ZoneId.of("UTC");
        ZonedDateTime date = LocalDateTime.parse(eventDateString).atZone(zone);
        this.eventDate = date;
    }

    public Integer getEventValue() {
        return eventValue;
    }

    public void setEventValue(Integer eventValue) {
        this.eventValue = eventValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLastUpdatedDate() {
        String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.lastUpdatedDate);
        return date;
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public SeriesModel getSerie() {
        return serie;
    }

    public void setSerie(SeriesModel serie) {
        this.serie = serie;
    }
}
