package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

@Entity
@Table(name= "TBL_USER_SERIES")
public class UserSeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_SERIES_ID")
    private Integer id;

    @Column(name = "USER_SERIES_PRIVILAGE")
    private UserPrivilege userPrivilage;

    @Column(name = "USER_SERIES_OWNER")
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

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserModel userModel;

}
