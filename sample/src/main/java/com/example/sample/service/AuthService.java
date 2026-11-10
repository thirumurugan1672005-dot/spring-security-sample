package com.example.sample.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sample.exception.DuplicateEmailException;
import com.example.sample.exception.PasswordTooLongException;
import com.example.sample.model.User;
import com.example.sample.repository.UserRepository;
import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                    savedUser.getEmail());
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("Email arleady exists");
        }

    }
}
