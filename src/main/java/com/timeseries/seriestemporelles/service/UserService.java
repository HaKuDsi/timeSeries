package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
    public Optional<UserModel> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public void saveOrUpdateUser(UserModel user) {
        Assert.notNull(user.getName(),"cannot save a null user");
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        Assert.notNull(id, "cannot fetch user with null id");
        userRepository.deleteById(id);
    }
}
