package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUserById(id).orElseThrow(() ->
                new ResourceNotFoundException("User: " + id + " is not found.")));
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody UserModel user) {
        try {
            Assert.notNull(user, "User can't be empty/null/blank");
            userService.saveOrUpdate(user);
            return new ResponseEntity("New user created with id: " + user.getId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Integer id,
                                     @RequestParam String user) {
        try {
            Assert.notNull(user, "User cannot be null");
            UserModel userUpdate = userService.getUserById(id).orElseThrow(() ->
                    new ResourceNotFoundException("User: " + id + " not found."));
            userUpdate.setName(user);
            userService.saveOrUpdate(userUpdate);
            return new ResponseEntity("User modified with id: " + userUpdate.getId(), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") Integer id) {
        try {
            Assert.notNull(id, "cannot fetch with null id");
            userService.delete(id);
            return new ResponseEntity("User delete with id: " + id, HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
