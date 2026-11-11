package com.example.sample.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration 
@EnableMethodSecurity 
public class SecurityConfig {
    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
       handler.setCsrfRequestAttributeName(null);
         http
         .csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(handler))
         .securityContext(sc->sc.securityContextRepository(securityContextRepository()))
         .cors(cors->cors.configurationSource(corsConfigurationSource()))
         .exceptionHandling(ex->ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
         .authorizeHttpRequests(auth-> auth
            .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/v1/auth/login")
            .permitAll()
            .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
            .requestMatchers("/error").permitAll()
            .anyRequest().authenticated())
            .logout(log->log.logoutUrl("/api/v1/auth/logout").logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).deleteCookies("JSESSIONID"));
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration =new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type","Accept","X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
        
        
    }
    @Bean
static RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("ADMIN").implies("USER")
            .build();
}
}
