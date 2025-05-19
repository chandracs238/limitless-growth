package com.pcs.limitless_growth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service // Marks this class as a Spring-managed service component
public class JwtService {

    // 👉 Secret key used to sign JWTs. In production, store this securely (e.g., in env vars or a vault)
    private static final String SECRET_KEY = "<REPLACE_ME_WITH_ENV_VAR_OR_SECRET_MANAGER>";

    /**
     * 🔐 Converts the secret key into a cryptographic signing key.
     * Required for HS256 signature verification.
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 🎯 Extracts the username (subject) from the JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 🕵️‍♂️ Extracts a specific claim using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 🧾 Parses and validates the JWT, then returns all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 🛠️ Generates a basic JWT for a user with no extra claims.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    /**
     * 🏗️ Generates a JWT with optional custom claims and user identity.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Add any custom claims (roles, metadata, etc.)
                .setSubject(userDetails.getUsername())
                // The "subject" of the token (typically the username)
                .setIssuedAt(new Date()) // When the token was created
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                // Expires in 24 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign using HS256 and your secret key
                .compact(); // Compress and encode the JWT
    }

    /**
     * 🧐 Validates a JWT: correct username and not expired.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * ⌛ Checks if the token has expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 🕰️ Extracts the expiration time of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
