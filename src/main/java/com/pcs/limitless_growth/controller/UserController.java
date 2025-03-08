package com.pcs.limitless_growth.controller;

import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password){
        boolean result = userService.login(username, password);
        if (result)
            return ResponseEntity.ok("Login Successful");
        return ResponseEntity.badRequest().body("Login Failed");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.registerUser(user);
        return ResponseEntity.ok("Register Successfully");
    }


}
