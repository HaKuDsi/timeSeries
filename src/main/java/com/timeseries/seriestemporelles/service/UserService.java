package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List getAllUsers() {
        List users = new ArrayList();
        userRepository.findAll().forEach(user -> users.add(user));
        return users;
    }

    public UserModel getUserById(int id) {
        return userRepository.findById(id).get();
    }

    public void saveOrUpdate(UserModel user) {
        userRepository.save(user);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
