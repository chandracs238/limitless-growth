package com.pcs.limitless_growth.repository;


import com.pcs.limitless_growth.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUserRepository(){
        userRepository.deleteAll();
        user = new User();
        user.setEmail("chandra@gmail.com");
        user.setUsername("chandrapcs");
        user.setPassword("pcs");
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists(){
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("chandrapcs");

        assertTrue(result.isPresent());
        assertEquals("chandrapcs", result.get().getUsername());
        assertEquals("chandra@gmail.com", result.get().getEmail());
    }


}
