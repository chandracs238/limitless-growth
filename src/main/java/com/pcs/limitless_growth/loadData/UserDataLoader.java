package com.pcs.limitless_growth.loadData;

import com.pcs.limitless_growth.entities.Tier;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("chandrapcs");
        user.setEmail("chandra@gmail.com");
        user.setPassword("pcs");
        user.setTier(Tier.E);
        user.setExpPoints(0L);

        userRepository.save(user);

    }
}
