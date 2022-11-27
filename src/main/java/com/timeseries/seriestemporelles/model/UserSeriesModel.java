package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

@Entity
@Table(name= "TBL_USER_SERIES")
public class UserSeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_SERIES_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserModel users;

    @ManyToOne
    @JoinColumn(name = "SERIE_ID")
    private SeriesModel series;

    @Column(name = "USER_SERIES_PRIVILAGE")
    private UserPrivilage userPrivilage;

    @Column(name = "USER_SERIES_OWNER")
    private Boolean owner;

    public UserSeriesModel() {
    }

    public int getId() {
        return id;
    }

    public UserModel getUsers() {
        return users;
    }

    public void setUsers(UserModel users) {
        this.users = users;
    }

    public SeriesModel getSeries() {
        return series;
    }

    public void setSeries(SeriesModel series) {
        this.series = series;
    }

    public UserPrivilage getUserPrivilege() {
        return userPrivilage;
    }

    public void setUserPrivilege(UserPrivilage userPrivilege) {
        this.userPrivilage = userPrivilege;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }
}
