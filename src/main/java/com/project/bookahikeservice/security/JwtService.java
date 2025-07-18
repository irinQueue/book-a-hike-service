package com.project.bookahikeservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.project.bookahikeservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;


    public String extractUsername(String token) {
        return getDecodedToken(token).getSubject();
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getEmail()) && !isTokenExpired(token);
    }

    public String generateToken(User userDetails) {
        return JWT.create()
                .withSubject(userDetails.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .sign(Algorithm.HMAC256(secret));
    }

    private boolean isTokenExpired(String token) {
        return getDecodedToken(token).getExpiresAt().before(new Date());
    }

    private DecodedJWT getDecodedToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }

}
