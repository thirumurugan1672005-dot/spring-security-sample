package com.example.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration 
public class SecurityConfig {
    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
         http.csrf(csrf->csrf.disable())
         .securityContext(sc->sc.securityContextRepository(securityContextRepository()))
         .exceptionHandling(ex->ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
         .authorizeHttpRequests(auth-> auth
            .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/v1/auth/login")
            .permitAll()
            .anyRequest().authenticated());
         return http.build();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean 
    public SecurityContextRepository securityContextRepository(){
         return new HttpSessionSecurityContextRepository();
    }
}
