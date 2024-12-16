package com.example.joinup.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Getter
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final Key key;
    private final long expirationTime;

    // 환경 변수 또는 설정 파일에서 SECRET_KEY와 EXPIRATION_TIME 불러오기
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expirationTime = expirationTime;
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증 및 사용자 이름 추출
    public String validateAndExtractUsername(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            logger.info("Token successfully validated for user: {}", claimsJws.getBody().getSubject());
            return claimsJws.getBody().getSubject();
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
            throw new SecurityException("토큰이 만료되었습니다.", e);
        } catch (MalformedJwtException e) {
            logger.error("Malformed token: {}", e.getMessage());
            throw new SecurityException("유효하지 않은 토큰 형식입니다.", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT: {}", e.getMessage());
            throw new SecurityException("지원되지 않는 JWT 형식입니다.", e);
        } catch (IllegalArgumentException e) {
            logger.error("Empty or null JWT token: {}", e.getMessage());
            throw new SecurityException("토큰이 비어 있거나 유효하지 않습니다.", e);
        } catch (JwtException e) {
            logger.error("Invalid token: {}", e.getMessage());
            throw new SecurityException("유효하지 않은 토큰입니다.", e);
        }
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid token: {}", e.getMessage());
        }
        return false; // 토큰이 유효하지 않으면 false 반환
    }

}
