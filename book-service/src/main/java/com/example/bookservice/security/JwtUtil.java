package com.example.bookservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {

    // 🔴 IMPORTANT: this must be EXACTLY the same as in auth-service
    private static final String SECRET_KEY = "MySuperSecretKeyForJwtDontShareveryconfidental";

    // Build a signing key object from the secret string
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Extract email (subject) from token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract role (we stored it as "role" claim in Auth service)
    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    public Long extractUserId(String token) {
        return Long.valueOf(
            extractAllClaims(token).get("userId").toString()
        );
    }
    // Validate the token (signature + format; expiry you can add later)
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);   // will throw if invalid
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
            // any other parsing error
            return false;
        }
    }

    // Private helper: parse token and get all claims
    private Claims extractAllClaims(String token) {
        // remove "Bearer " if someone accidentally passes full header
        String rawToken = token.replace("Bearer ", "");

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(rawToken)
                .getBody();
    }
}