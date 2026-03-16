package org.maaky1.fintrack.util;

import java.util.Date;
import javax.crypto.SecretKey;
import org.maaky1.fintrack.configuration.AppProperties;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final AppProperties appProperties;

    public String generateToken(String UID) {
        return Jwts.builder()
                .issuer(appProperties.getAPP_NAME())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + appProperties.getJWT_EXPIRATION()))
                .subject(UID)
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUID(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT error cause {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJWT_SECRET_KEY());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
