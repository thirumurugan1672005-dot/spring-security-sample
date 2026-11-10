package com.example.sample.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import com.example.sample.exception.DuplicateEmailException;
import com.example.sample.exception.PasswordTooLongException;
import com.example.sample.model.User;
import com.example.sample.repository.UserRepository;
import com.example.sample.request.LoginRequest;
import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse registerUser(RegistrationRequest request) {
        // process password
        String password = request.getPassword();
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new PasswordTooLongException("Password too long");
        }
        // process email
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = request.getUsername().trim();

        // check if exists by email
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("User already exists");
        }

        // hash password
        String passwordHash = passwordEncoder.encode(password);
        User userToSave = new User(username, email, passwordHash);
        try {

            // save the user
            User savedUser = userRepository.save(userToSave);
            UserResponse response = new UserResponse(savedUser.getUserId(), savedUser.getUsername(),
                    savedUser.getEmail(),savedUser.getRole());
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("Email arleady exists");
        }

    }

    public Authentication login(LoginRequest request){
         String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
         String password = request.getPassword();

         var token = UsernamePasswordAuthenticationToken.unauthenticated(email, password);
         var result  = authenticationManager.authenticate(token);
          return  result;
         
    }
}
