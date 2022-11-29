package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
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
    public List getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public UserModel getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id).orElseThrow(() ->
                new ResourceNotFoundException("User: " + id + "is not found."));
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestParam String userName) {
        try {
            UserModel user = new UserModel(userName);
            userService.saveOrUpdate(user);
            return new ResponseEntity("New user created with id: " + user.getId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Integer id,
                                      @RequestParam String userName) {
        try {
            UserModel user = userService.getUserById(id).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + id + " not found."));
            user.setName(userName);
            userService.saveOrUpdate(user);
            return new ResponseEntity("User modified with id: " + user.getId(), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteById(@PathVariable("id") int id) {
        try {
            userService.delete(id);
            return new ResponseEntity("User delete with id: " + id, HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
