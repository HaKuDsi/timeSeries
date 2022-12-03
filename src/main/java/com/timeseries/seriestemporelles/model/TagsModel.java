package com.timeseries.seriestemporelles.model;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "TBL_TAGS")
public class TagsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TAGS_ID")
    private Integer id;

    private String label;

    private ZonedDateTime lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private EventModel event;

    public TagsModel() {
    }

    public TagsModel(Integer id, String label, EventModel event) {
        this.id = id;
        this.label = label;
        this.event = event;
        this.setLastUpdatedDate();
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLastUpdatedDate() {
        String date = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm z", Locale.FRANCE).format(this.lastUpdatedDate);
        return date;
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }
}
