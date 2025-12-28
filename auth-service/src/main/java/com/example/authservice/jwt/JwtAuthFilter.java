package com.example.authservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️ Get the "Authorization" header from the incoming request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2️⃣ Check if token is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);           // Remove "Bearer " and get the token
            username = jwtService.extractUsername(token); // Extract username/email from token
        }

        // 3️⃣ If we got a username AND no authentication is set yet for this request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4️⃣ Load user details (like roles) from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5️⃣ Check if the token is valid for this user
            if (jwtService.isTokenValid(token)) {

                // 6️⃣ Create an Authentication object with user details & roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 7️⃣ Store this Authentication inside the SecurityContext (mark user as logged in for this request)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8️⃣ Continue with the remaining filters or finally reach the controller
        filterChain.doFilter(request, response);
    }
}