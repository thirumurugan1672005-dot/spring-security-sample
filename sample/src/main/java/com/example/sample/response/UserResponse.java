package com.example.sample.response;

import java.util.UUID;

import com.example.sample.model.Role;

import com.example.sample.security.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter 
public class UserResponse {
    private UUID userId;
    private String username;
    private String email;
    private Role role;

    public static  UserResponse fromUserDetails(CustomUserDetails userDetails){
        UserResponse response = new UserResponse(userDetails.getUserId(),userDetails.getDisplayName(),userDetails.getEmail(),userDetails.getRole());
        return response;
    }
}
