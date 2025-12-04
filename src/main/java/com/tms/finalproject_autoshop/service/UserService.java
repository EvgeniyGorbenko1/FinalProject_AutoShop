package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Service
public class UserService {

    private final UserRepository userRepository;
    //private final SecurityService securityServic

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.securityService = securityService;
    }

    public Boolean createUser(UserCreateDto userCreateDto) {
        try {
            User newUser = new User();
            newUser.setFirstName(userCreateDto.getFirstName());
            newUser.setLastName(userCreateDto.getSecondName());
            newUser.setEmail(userCreateDto.getEmail());
            newUser.setAge(userCreateDto.getAge());
            newUser.setCreated(LocalDateTime.now());
            newUser.setUpdated(LocalDateTime.now());
            userRepository.save(newUser);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);

    }

    public Optional<User> updateUser(User user) {
        Optional<User> updateUser = getUserById(user.getId());
        if (updateUser.isPresent()) {
                return Optional.of(userRepository.saveAndFlush(user));
        } else {
            throw new NullPointerException();
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean deleteUser(Long id) {
            if(userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return true;
            } else{
                throw new NullPointerException(); //TODO: exception
            }
    }


}