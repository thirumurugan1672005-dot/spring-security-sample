package com.example.sample.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.sample.model.User;
import com.example.sample.repository.UserRepository;
import com.example.sample.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByEmail(username.trim().toLowerCase(Locale.ROOT));

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOpt.get();
        CustomUserDetails userDetails = new CustomUserDetails(user.getUserId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
        return userDetails;

    }

}
