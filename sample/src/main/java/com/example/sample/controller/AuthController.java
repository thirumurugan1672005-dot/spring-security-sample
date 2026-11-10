package com.example.sample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.request.LoginRequest;
import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;
import com.example.sample.security.CustomUserDetails;
import com.example.sample.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

     private final AuthService authService;
     private final SecurityContextRepository securityContextRepository;
     public AuthController(AuthService authService,SecurityContextRepository securityContextRepository){
         this.authService = authService;
         this.securityContextRepository = securityContextRepository;
     }
    @PostMapping ("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid  @RequestBody  RegistrationRequest request){
           UserResponse response = authService.registerUser(request);
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping ("/login")
    public void loginUser(@Valid  @RequestBody  LoginRequest loginRequest,HttpServletRequest request,HttpServletResponse response){
        Authentication authentication = authService.login(loginRequest);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        
        // checks session exists or not 
        // if exists change session
        if(request.getSession(false) != null){
                request.changeSessionId();
        }

        securityContextRepository.saveContext(securityContext, request, response);
        
    }
    @GetMapping ("/me")
    public ResponseEntity<UserResponse> getLoggedInUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        UserResponse response = new UserResponse(userDetails.getUserId(), userDetails.getDisplayName(),userDetails.getUsername(),userDetails.getRole());
        return ResponseEntity.ok().body(response);
    }
}
