package com.example.sample.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class LoginSessionHandler{
    private final SecurityContextRepository securityContextRepository;
    public LoginSessionHandler(SecurityContextRepository securityContextRepository){
        this.securityContextRepository = securityContextRepository;
    }
    public void startSession(Authentication authentication,HttpServletRequest request,HttpServletResponse response){
          SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        
        // checks session exists or not 
        // if exists change session
        if(request.getSession(false) != null){
                request.changeSessionId();
        }

        securityContextRepository.saveContext(securityContext, request, response);
    }
}