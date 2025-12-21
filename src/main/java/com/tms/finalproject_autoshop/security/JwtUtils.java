package com.tms.finalproject_autoshop.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class JwtUtils {
    @Value("60")
    private String jwtExpirationMinutes;
@Value("d7e0ff8c3b8fcacb5c4e3ea330b962e25f14096520995778cf7a18f61188f156714d9622006b1b42c13602f44760356992cb8fa5f4862cc8c5f3eef639ee78bb")
    private String secretKey;

    public String getToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Integer.parseInt(jwtExpirationMinutes))))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        log.info("IN JwtUtils::validateToken");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException e){
            log.error(e.getMessage());
            return false;
        } finally {
            log.info("IN JwtUtils::validateToken");
        }
        return true;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
