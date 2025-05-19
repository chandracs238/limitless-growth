package com.pcs.limitless_growth.entities;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ⚠️ Assumes user has a single role. You can expand this for multiple roles.
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 🔐 Spring uses this for matching with login input
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 🧑‍💻 Used during authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 🗓️ You can wire this to a "validUntil" date in User entity
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 🚫 Could hook this to a `user.isLocked()` flag
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 🔐 Useful if you enforce password expiry policies
    }

    @Override
    public boolean isEnabled() {
        return true; // ✅ Typically matches a `user.isActive()` or `user.isEnabled()`
    }
}
