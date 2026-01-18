package com.fullstack.authservice.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    private final Key signingKey;

    public AuthController(@Value("${auth.jwt.secret:defaultsecretdefaultsecretdefaultsecret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        // Simple demo - accept any username/password and return a token with claims
        String username = req.getOrDefault("username", "user");
        String roles = "USER";
        long now = System.currentTimeMillis();
        Date exp = new Date(now + 3600_000); // 1 hour
        String jws = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("scope", "USER")
                .claim("tenantId", "default")
                .setExpiration(exp)
                .setIssuedAt(new Date(now))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
        return ResponseEntity.ok(Map.of("access_token", jws, "token_type", "bearer", "expires_in", 3600));
    }
}

