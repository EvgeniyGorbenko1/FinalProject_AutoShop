package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findCartById(Long id);
}
