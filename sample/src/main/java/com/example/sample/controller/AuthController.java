package com.example.sample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.request.LoginRequest;
import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;
import com.example.sample.security.CustomUserDetails;
import com.example.sample.security.LoginSessionHandler;
import com.example.sample.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

     private final AuthService authService;
     private final LoginSessionHandler loginSessionHandler;
     public AuthController(AuthService authService,LoginSessionHandler loginSessionHandler){
         this.authService = authService;
         this.loginSessionHandler = loginSessionHandler;
     }
    @PostMapping ("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid  @RequestBody  RegistrationRequest request){
           UserResponse response = authService.registerUser(request);
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping ("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid  @RequestBody  LoginRequest loginRequest,HttpServletRequest request,HttpServletResponse response){
        Authentication authentication = authService.login(loginRequest);
        loginSessionHandler.startSession(authentication, request, response);
        CustomUserDetails details =(CustomUserDetails)authentication.getPrincipal();
        UserResponse userResponse = UserResponse.fromUserDetails(details);
        return ResponseEntity.ok().body(userResponse);
    }
    @GetMapping ("/me")
    public ResponseEntity<UserResponse> getLoggedInUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        UserResponse response = UserResponse.fromUserDetails(userDetails);
        return ResponseEntity.ok().body(response);
    }
}
