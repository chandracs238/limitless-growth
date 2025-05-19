package com.pcs.limitless_growth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Tells Spring this is a config class – think: central wiring hub.
@EnableWebSecurity // Enables Spring Security for this app.
@RequiredArgsConstructor // Lombok magic: autogenerates a constructor for final fields.
public class SecurityConfig {

    // Injected service to load user-specific data for authentication (e.g., from DB)
    private final CustomUserDetailsService userDetailsService;

    // JWT filter to intercept requests and validate tokens before reaching secured endpoints
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Provides the AuthenticationManager used for authenticating credentials.
     * Required when doing manual authentication flows (e.g., login endpoints).
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Delegates to Spring's internal config
    }

    /**
     * Password encoder bean. BCrypt is widely used and strong (adaptive hash).
     * Swap to Argon2 if you want something more modern (and heavier).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Good default for most apps
    }

    /**
     * Authentication provider that uses our custom user service and password encoder.
     * This wires how Spring will actually verify username/password combos.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Loads user from DB or other source
        provider.setPasswordEncoder(passwordEncoder()); // Ensures hashed password matching
        return provider;
    }

    /**
     * Defines the core security rules for the app.
     * This is where we say: which routes are public, how sessions work, and where JWT fits in.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless apps (e.g., APIs that use JWTs)
                .csrf(AbstractHttpConfigurer::disable)

                // Don't store sessions – each request must carry its own token (JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define which requests are public vs. which need authentication
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to these endpoints (public roads)
                        .requestMatchers(
                                "/api/auth/**",     // Login, register, etc.
                                "/swagger-ui/**",      // API docs UI
                                "/v3/api-docs/**",     // Swagger backend
                                "/swagger-ui.html"     // Swagger UI page
                        ).permitAll()
                        // Everything else requires authentication (guarded gates)
                        .anyRequest().authenticated())

                // Wire in our custom authentication provider for username/password login
                .authenticationProvider(authenticationProvider())

                // Ensure JWT filter runs *before* Spring's default login filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Returns the configured security chain
    }
}
