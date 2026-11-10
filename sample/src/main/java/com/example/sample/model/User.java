package com.example.sample.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity 
@Table (name = "users")
@Getter 
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @NotBlank(message = "username cannot be empty")
    @Column (nullable =  false)
    private String username;

    @NotBlank(message = "email cannot be empty")
    @Email (message =  "invalid email address")
    @Column (nullable =  false , unique = true)
    private String email;

    @NotBlank (message =  "password cannot be empty")
    @Column (nullable =  false)
    private String password;

    @NotNull  (message =  "role cannot be empty")
    @Enumerated(EnumType.STRING)
    @Column (nullable =  false)
    private Role role = Role.ROLE_USER;

    protected  User(){

    }

    public User(String username,String email,String password){
         this.username = username;
         this.email = email;
         this.password = password;
    }
}
