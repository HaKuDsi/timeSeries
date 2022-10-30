package com.timeseries.seriestemporelles.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "TBL_USER_SERIES")
public class UserSeriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_SERIES_ID")
    private Integer id;

    @Column(name = "USER_SERIES_PRIVILAGE")
    private UserPrivilege userPrivilege;

    @Column(name = "USER_SERIES_OWNER")
    private Boolean owner;

    public UserSeriesModel() {
    }

    public Integer getId() {
        return id;
    }

    public UserPrivilege getUserPrivilege() {
        return userPrivilege;
    }

    public void setUserPrivilege(String userPrivilege) {
        this.userPrivilege = UserPrivilege.valueOf(userPrivilege);
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

    @OneToMany
    private Set<SeriesModel> series = new HashSet<>();

}
