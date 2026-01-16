package com.example.bookservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // PUBLIC
                .requestMatchers("/api/books/ping").permitAll()
                .requestMatchers("/api/books/debug").permitAll() // TEMP

                // BOOK APIs (🔥 THIS FIXES IT 🔥)
                .requestMatchers("/api/books/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                // ADMIN
                .requestMatchers("/api/admin/**")
                    .hasAuthority("ROLE_ADMIN")

                // STAFF
                .requestMatchers("/api/staff/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                // STUDENT
                .requestMatchers("/api/student/**")
                    .hasAuthority("ROLE_STUDENT")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

