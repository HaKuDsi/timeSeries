package com.timeseries.seriestemporelles.model;

import javax.persistence.*;

@Entity
@Table(name= "TBL_USER")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    public UserModel(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel() {
    }
/*
    @OneToMany(mappedBy = "USER_SERIES")
    private Set<UserSeriesModel> userSeries = new HashSet<>();
 */
}
