package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserModel, Integer> {
}
