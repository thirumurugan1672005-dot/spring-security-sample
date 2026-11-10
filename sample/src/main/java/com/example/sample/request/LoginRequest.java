package com.example.sample.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter 
public class LoginRequest {
    @NotBlank (message = "email cannot be empty")
    @Size(max = 255)
    private String email;
    @NotBlank (message = "password cannot be empty")
    @Size (max = 72)
    private String password;
}
