package com.example.sample.request;





import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter 
@AllArgsConstructor 
@NoArgsConstructor 
public class RegistrationRequest {
    @NotBlank (message = "username cannot be empty")
    @Size(min = 2 , max=64)
    private String username;
    @NotBlank (message = "email cannot be empty")
    @Email (message = "invalid email address")
    private String email;
    @NotBlank (message = "password cannot be empty")
    @Size  (min = 8, max=72)
    private String password;



}
