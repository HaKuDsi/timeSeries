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

    public UserSeriesModel(Integer id, UserModel users, SeriesModel series, UserPrivilage userPrivilage, Boolean owner) {
        this.id = id;
        this.users = users;
        this.series = series;
        this.userPrivilage = userPrivilage;
        this.owner = owner;
    }

    public Integer getId() {
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


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        UserSeriesModel other = (UserSeriesModel) obj;
        if(id == null) {
            if(other.id != null)
                return false;
        } else if (!id.equals(other.id))
                return false;
    return true;
    }
}
