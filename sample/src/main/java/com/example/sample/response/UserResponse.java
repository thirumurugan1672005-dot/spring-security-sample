package com.example.sample.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter 
public class UserResponse {
    private UUID userId;
    private String username;
    private String email;
}
