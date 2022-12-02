package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
    @Cacheable("user")
    public Optional<UserModel> getUserById(int id) {
        return userRepository.findById(id);
    }

    public void saveOrUpdate(UserModel user) {
        Assert.notNull(user,"cannot save a null object");
        userRepository.save(user);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
