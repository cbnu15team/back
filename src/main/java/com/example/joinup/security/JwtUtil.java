package com.example.joinup.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Getter
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 더 안전한 환경 변수 또는 설정 파일에서 SECRET_KEY 로드 추천
    private static final String SECRET_KEY = "your-secure-secret-key-for-jwt-more-secure-and-longer";
    private static final long EXPIRATION_TIME = 86400000; // 만료시간 하루

    // JWT 키 반환
    private final Key key;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // JWT 생성 메서드
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired. Claims: {}", e.getClaims());
        } catch (MalformedJwtException e) {
            logger.error("Malformed token: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid token: {}", e.getMessage());
        }
        return false; // 예외가 발생하면 false 반환
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            logger.error("Failed to extract username: {}", e.getMessage());
            return null;
        }
    }

}
