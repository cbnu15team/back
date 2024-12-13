package com.example.joinup;

import com.example.joinup.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateToken() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtUtil.generateToken(username);

        // Assert
        assertNotNull(token); // 토큰이 null이 아니어야 함
        System.out.println("Generated Token: " + token);
    }

    @Test
    public void testValidateToken() {
        // Arrange
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        // Act
        boolean isValid = jwtUtil.validateToken(token);
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertTrue(isValid); // 토큰이 유효해야 함
        assertEquals(username, extractedUsername); // 토큰에서 추출한 사용자명과 일치해야 함
        System.out.println("Extracted Username: " + extractedUsername);
    }

    @Test
    public void testExpiredToken() {
        // Arrange
        String expiredToken = Jwts.builder()
                .setSubject("expiredUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1시간 전
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // 만료된 시간
                .signWith(jwtUtil.getKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act
        boolean isValid = jwtUtil.validateToken(expiredToken);

        // Assert
        assertFalse(isValid);
        System.out.println("Expired Token is valid: " + isValid);
    }


    @Test
    public void testExtractUsername() {
        // Arrange
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername); // 토큰에서 추출한 사용자명과 일치해야 함
        System.out.println("Extracted Username: " + extractedUsername);
    }
}