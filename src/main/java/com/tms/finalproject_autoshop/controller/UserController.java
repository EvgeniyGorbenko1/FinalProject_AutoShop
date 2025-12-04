package com.tms.finalproject_autoshop.controller;


import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "name")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto user){
        Boolean result = userService.createUser(user);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ("id") Long id) throws AccessDeniedException {
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        Optional<User> updatedUser = userService.updateUser(user);
        if(updatedUser.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> user = userService.getAllUsers();
        if(user.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id){
        Boolean result = userService.deleteUser(id);
        if(!result){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
