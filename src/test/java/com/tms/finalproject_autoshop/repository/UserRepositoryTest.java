package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("FIRSTNAME");
        user.setLastName("LASTNAME");
        user.setEmail("test@mail.com");
        user.setAge(20);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
    }

    @Test
    void saveTest_Success() {
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("FIRSTNAME", saved.getFirstName());
    }

    @Test
    void findByIdTest_Success() {
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void findAllTest_Success() {
        userRepository.save(user);

        List<User> users = userRepository.findAll();

        assertFalse(users.isEmpty());
    }

    @Test
    void updateTest_Success() {
        User saved = userRepository.save(user);

        saved.setFirstName("UPDATED");
        User updated = userRepository.saveAndFlush(saved);

        assertEquals("UPDATED", updated.getFirstName());
    }

    @Test
    void deleteByIdTest_Success() {
        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
