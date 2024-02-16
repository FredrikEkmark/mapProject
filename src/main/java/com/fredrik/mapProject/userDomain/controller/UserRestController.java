package com.fredrik.mapProject.userDomain.controller;

import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fredrik.mapProject.config.AppPasswordConfig;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserRepository userRepository;
    private final AppPasswordConfig appPasswordConfig; // Bcrypt

    @Autowired
    public UserRestController(UserRepository userRepository, AppPasswordConfig appPasswordConfig) {
        this.userRepository = userRepository;
        this.appPasswordConfig = appPasswordConfig;
    }

}
