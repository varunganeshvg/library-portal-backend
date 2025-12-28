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

            	    // Admin-only APIs
            	    .requestMatchers("/api/admin/**")
            	        .hasAuthority("ROLE_ADMIN")

            	    // Admin + Staff APIs
            	    .requestMatchers("/api/staff/**")
            	        .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

            	    // Student-only APIs  ✅ FIXED LINE
            	    .requestMatchers("/api/student/**")
            	        .hasAuthority("ROLE_STUDENT")

            	    // Everything else needs login
            	    .anyRequest().authenticated()
            	)

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


