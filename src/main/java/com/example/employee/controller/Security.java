package com.example.employee.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simpler form submission
                .authorizeHttpRequests(auth -> auth
                        // Allow everyone to access the login, register, and static assets
                        .requestMatchers("/", "/admin/login", "/admin/register", "/css/**", "/js/**").permitAll()
                        // Your manual session checks in Controller handle the rest
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable()) // Disable default login page
                .logout(logout -> logout.disable()); // Disable default logout

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Required for password hashing
    }
}