package com.pcs.limitless_growth.security;

import com.pcs.limitless_growth.entities.CustomUserDetails;
import com.pcs.limitless_growth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to authenticate user: {}", username);

        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new) // 🔁 wrap JPA user into Spring Security interface
                .orElseThrow(() -> {
                    log.warn("User '{}' not found in the database", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }
}
