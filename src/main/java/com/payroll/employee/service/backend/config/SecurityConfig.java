package com.payroll.employee.service.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Disable CSRF
                    .authorizeHttpRequests(authorize ->
                            authorize
                                    .requestMatchers("/h2-console/**").permitAll() // Permit all requests to H2 console
                                    .anyRequest().authenticated() // Protect other requests
                    )
                    .headers(headers -> headers.frameOptions().sameOrigin()) // Allow frames from the same origin
                    .httpBasic(Customizer.withDefaults()); // Enable HTTP Basic authentication

            return http.build();
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails mark = User.builder()
                    .username("mark")
                    .password(passwordEncoder().encode("123"))
                    .roles("USER")
                    .build();

            UserDetails admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("123"))
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(mark, admin);
        }
    }



