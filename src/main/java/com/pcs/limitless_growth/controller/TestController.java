package com.pcs.limitless_growth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint!";
    }

    @GetMapping("/user")
    public String userEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "Hello, User! Your email is: " + jwt.getClaim("email");
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello, Admin!";
    }
}

