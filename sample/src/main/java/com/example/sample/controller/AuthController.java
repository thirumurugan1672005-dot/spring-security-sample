package com.example.sample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;
import com.example.sample.service.AuthService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

     private final AuthService authService;
     public AuthController(AuthService authService){
         this.authService = authService;
     }
    @PostMapping ("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid  @RequestBody  RegistrationRequest request){
           UserResponse response = authService.registerUser(request);
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
