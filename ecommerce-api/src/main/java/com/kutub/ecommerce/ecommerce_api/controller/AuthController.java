package com.kutub.ecommerce.ecommerce_api.controller;

import com.kutub.ecommerce.ecommerce_api.dto.*;
import com.kutub.ecommerce.ecommerce_api.entity.Role;
import com.kutub.ecommerce.ecommerce_api.entity.User;
import com.kutub.ecommerce.ecommerce_api.mapper.UserMapper;
import com.kutub.ecommerce.ecommerce_api.repository.RoleRepository;
import com.kutub.ecommerce.ecommerce_api.repository.UserRepository;
import com.kutub.ecommerce.ecommerce_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(), 
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        // লগইন করা ইউজারের ডাটা বের করছি
        User user = userRepository.findByUsername(authentication.getName())
                .orElseGet(() -> userRepository.findByEmail(authentication.getName()).orElseThrow());
        
        AuthResponse authResponse = new AuthResponse(jwt, userMapper.toDTO(user));
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(ApiResponse.error("Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(ApiResponse.error("Email is already in use!"), HttpStatus.BAD_REQUEST);
        }

        // ১. ইউজার সেভ করা
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        String rawPassword = registerRequest.getPassword(); // টোকেন তৈরির জন্য লাগবে
        user.setPassword(passwordEncoder.encode(rawPassword));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);

        // ২. অটোমেটিক লগইন (Auto-Login)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), rawPassword)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // ৩. রেসপন্সে টোকেন এবং ইউজার ডিটেইল পাঠানো
        AuthResponse authResponse = new AuthResponse(jwt, userMapper.toDTO(savedUser));

        return ResponseEntity.ok(ApiResponse.success("User registered and logged in successfully", authResponse));
    }
}
