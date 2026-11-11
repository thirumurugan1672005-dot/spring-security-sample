package com.example.sample.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sample.exception.DuplicateEmailException;
import com.example.sample.exception.PasswordTooLongException;
import com.example.sample.model.User;
import com.example.sample.repository.UserRepository;
import com.example.sample.request.LoginRequest;
import com.example.sample.request.RegistrationRequest;
import com.example.sample.response.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

    private static final int BCRYPT_MAX_BYTES = 72;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse registerUser(RegistrationRequest request) {
        String password = request.getPassword();
        if (exceedsBcryptLimit(password)) {
            throw new PasswordTooLongException("Password too long");
        }

        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = request.getUsername().trim();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(password);
        User userToSave = new User(username, email, passwordHash);

        try {
            User savedUser = userRepository.save(userToSave);
            return new UserResponse(savedUser.getUserId(), savedUser.getUsername(),
                    savedUser.getEmail(), savedUser.getRole());
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate registration attempt");
            throw new DuplicateEmailException("Email already exists");
        }
    }

    public Authentication login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String password = request.getPassword();

        if (exceedsBcryptLimit(password)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        var token = UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return authenticationManager.authenticate(token);
    }

    private boolean exceedsBcryptLimit(String password) {
        return password.getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_BYTES;
    }
}