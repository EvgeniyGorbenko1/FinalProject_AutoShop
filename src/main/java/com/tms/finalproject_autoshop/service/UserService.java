package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.exception.CustomException;
import com.tms.finalproject_autoshop.model.Security;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.AuthRequest;
import com.tms.finalproject_autoshop.model.dto.AuthResponse;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.model.dto.UserUpdateDto;
import com.tms.finalproject_autoshop.repository.SecurityRepository;
import com.tms.finalproject_autoshop.repository.UserRepository;
import com.tms.finalproject_autoshop.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, SecurityRepository securityRepository, BCryptPasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
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
            return true;
        } catch (Exception ex) {
            log.error("Error in saving user: " + ex.getMessage());
            return false;
        }

    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Boolean updateUser(UserUpdateDto userUpdateDto, Long id) {
        Optional<User> updatedUser = getUserById(id);
        if (updatedUser.isPresent()) {
            updatedUser.get().setFirstName(userUpdateDto.getFirstName());
            updatedUser.get().setLastName(userUpdateDto.getLastName());
            updatedUser.get().setEmail(userUpdateDto.getEmail());
            userRepository.save(updatedUser.get());
            return true;
        } else {
            log.error("User not found");
            return false;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Boolean deleteUser(Long id) {
        try {
            Optional<User> user = getUserById(id);
            if (user.isEmpty()) {
                return false;
            }
            securityRepository.delete(user.get().getSecurity());
            userRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            log.error("User not found with id: " + id);
            return false;
        }
    }


    public List<User> getSortedUsersByField(String field, String order) {
        if (order != null && !order.isBlank() && order.equals("desk")) {
            return userRepository.findAll(Sort.by(Sort.Direction.DESC, field));
        }
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }


    public Page<User> getAllUsersWithPagination(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public AuthResponse loginUser(AuthRequest authRequest) {
        log.info("Attempting to log in user: {}", authRequest.getUsername());

        Security security = (Security) securityRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new CustomException("Invalid username or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), security.getPassword())) {
            log.error("Invalid password for login: {}", authRequest.getUsername());
            throw new CustomException("Invalid username or password");
        }

        String token = jwtUtils.getToken(security.getUsername());

        log.info("User '{}' successfully logged in.", security.getUsername());
        return new AuthResponse(token);
    }

}