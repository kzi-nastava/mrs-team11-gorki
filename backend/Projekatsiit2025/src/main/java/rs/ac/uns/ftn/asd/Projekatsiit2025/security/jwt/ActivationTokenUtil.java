package rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class ActivationTokenUtil {

    @Value("${app.jwtActivationSecret:NEKI_AKTIVACIONI_TAJNI_KLJUC_KOJI_JE_DOVOLJNO_DUGACAK}")
    private String secret;

    private final long expirationMs = 24L * 60 * 60 * 1000; // 24h

    public String generateActivationToken(String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(exp)
                .claim("type", "ACTIVATION")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String validateAndGetEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (!"ACTIVATION".equals(claims.get("type", String.class))) {
            throw new RuntimeException("Wrong token type");
        }

        return claims.getSubject();
    }
}