package com.example.joinup.user.service;

import com.example.joinup.security.JwtUtil;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil; // JwtUtil 주입
    }

    @Transactional
    public User registerUser(User user) {
        // 로그인 ID 중복 확인
        boolean exists = userRepository.findById(user.getId()).isPresent(); // 적절한 ID 체크 로직 사용
        if (exists) {
            throw new RuntimeException("이미 존재하는 ID입니다: " + user.getId());
        }

        // 사용자 저장
        return userRepository.save(user);
    }

    @Transactional
    public User login(String id, String password) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new Exception("해당 ID가 존재하지 않습니다.");
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        return user; // 로그인 성공 시 유저 객체 반환
    }

    // JWT 토큰 생성
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getId());
    }

    @Transactional(readOnly = true)
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
    }

}
