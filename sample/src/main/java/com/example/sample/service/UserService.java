package com.example.sample.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sample.model.User;
import com.example.sample.repository.UserRepository;
import com.example.sample.request.RegistrationRequest;

@Service 
public class UserService {
     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;

     public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
         this.userRepository = userRepository;
         this.passwordEncoder = passwordEncoder;
     }

     public User registerUser(RegistrationRequest request){
         // process email
         String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
         String username = request.getUsername().trim();

         // check if exists by email
         if(userRepository.existsByEmail(email)){
             throw new RuntimeException("User already exists");
         }

         String password = request.getPassword();
         if(password.getBytes()!= null && password.getBytes().length > 72){
             throw new RuntimeException("Exceeds byte size provide smaller one");
         }

         // encrypt password 
         String passwordHash = passwordEncoder.encode(request.getPassword());

         User userToSave = new User(username,email,passwordHash);

         // save the user 
         User savedUser = userRepository.save(userToSave);

         return savedUser;
         
     }
}
