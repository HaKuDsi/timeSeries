package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

@Entity
@Table(name= "TBL_USER_SERIES")
public class UserSeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private UserPrivilege userPrivilage;
    private Boolean owner;

    public UserSeriesModel() {
    }

    public Integer getId() {
        return id;
    }

    public UserPrivilege getUserPrivilage() {
        return userPrivilage;
    }

    public void setUserPrivilage(UserPrivilege userPrivilage) {
        this.userPrivilage = userPrivilage;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }
}
