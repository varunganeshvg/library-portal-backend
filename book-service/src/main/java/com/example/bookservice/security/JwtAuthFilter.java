package com.example.bookservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
//@Order(1)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(">>> JwtAuthFilter HIT");

        String authHeader = request.getHeader("Authorization");
        System.out.println(">>> Authorization header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 Extract data from JWT
        String email = jwtUtil.extractEmail(token);
        String role  = jwtUtil.extractRole(token);
        Long userId  = jwtUtil.extractUserId(token);   // ✅ NEW

        System.out.println(">>> Email = " + email);
        System.out.println(">>> Role  = " + role);
        System.out.println(">>> UserId = " + userId);

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(authority)
                );

        // 🔥 STORE userId INSIDE AUTHENTICATION
        authentication.setDetails(userId);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(">>> Authentication SET with userId");

        filterChain.doFilter(request, response);
    }
}
