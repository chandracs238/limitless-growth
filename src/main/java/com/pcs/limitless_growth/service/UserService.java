package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.entities.Tier;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(String username, String email, String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setTier(Tier.E);
        user.setExpPoints(0L);
        userRepository.save(user);
    }

    public boolean login(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return user.getPassword().equals(password);
    }
}
