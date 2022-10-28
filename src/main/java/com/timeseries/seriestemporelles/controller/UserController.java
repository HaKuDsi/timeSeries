package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    private List getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    private UserModel getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    private ResponseEntity createUser(@RequestBody UserModel user) {
        try {
            userService.saveOrUpdate(user);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("New user created with id: " + user.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    private ResponseEntity deleteById(@PathVariable("id") int id) {
        try {
            userService.delete(id);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("User delete with id: " + id, HttpStatus.OK);
    }
}
