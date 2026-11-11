package com.example.sample.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sample.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @GetMapping("/reports/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly(@AuthenticationPrincipal CustomUserDetails me) {
        return "Admin report for " + me.getDisplayName();
    }

    @GetMapping("/user/hello")
    @PreAuthorize("hasRole('USER')")
    public String userHello(@AuthenticationPrincipal CustomUserDetails me) {
        return "Hello " + me.getDisplayName() + " (" + me.getRole() + ")";
    }
}