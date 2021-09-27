package com.miw.server.controller;

import com.miw.server.domain.User;
import com.miw.server.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/authenticate")
    public ResponseEntity authenticateUser(@RequestBody User user) {
        // use is authenticated default now
        if (authenticationManager.authenticate(null) != null){
            return ResponseEntity.ok(JwtUtils.generateToken(user));
        }
        return new ResponseEntity("User cannot be authenticated", HttpStatus.BAD_REQUEST);
    }
}
