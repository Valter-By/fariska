package com.fufa.fariska.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String checkGoogleUser() {
        return "New user!";
    }

}
