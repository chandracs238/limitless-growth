package com.pcs.limitless_growth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 🔍 Step 1: Grab the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // ⛔ Step 2: If it's missing or doesn't start with "Bearer ", skip filtering
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🪄 Step 3: Extract the JWT token from the header
        final String jwtToken = authHeader.substring(7); // Skip "Bearer "

        // 🕵️‍♂️ Step 4: Extract the username from the token
        String username = null;
        try {
            username = jwtService.extractUsername(jwtToken);
        } catch (Exception e) {
            // Optional: log the exception or handle it gracefully
            filterChain.doFilter(request, response);
            return;
        }

        // 🔒 Step 5: If user not already authenticated, validate the token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 🧠 Step 6: Load user details (calls your CustomUserDetailsService)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ Step 7: If token is valid, create an auth object and set it in the context
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // attach user roles/permissions
                );

                // 📦 Attach request-specific details (IP, session ID, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 🔑 Store authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 🚀 Step 8: Let the request proceed through the filter chain
        filterChain.doFilter(request, response);
    }
}
