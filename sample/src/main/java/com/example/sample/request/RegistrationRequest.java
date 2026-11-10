package com.example.sample.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "username cannot be empty")
    @Size(min = 2, message = "username should have at least {min} characters")
    @Size(max = 64, message = "username should be at most {max} characters")
    private String username;

    @Size(max = 254, message = "email must be at most {max} characters")
    @NotBlank(message = "email cannot be empty")
    @Email(message = "invalid email address")
    private String email;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, message = "password must be at least {min} characters")
    @Size(max = 72, message = "password must be at most {max} characters")
    private String password;

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

}
