package com.fullstack.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Collections;

public class JwtTokenValidator {

    private final Key signingKey;

    public JwtTokenValidator(String secret) {
        // in production use a proper key management
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validate(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            Date exp = claims.getBody().getExpiration();
            return exp == null || exp.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public Map<String, Object> getClaims(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody();
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }
}
