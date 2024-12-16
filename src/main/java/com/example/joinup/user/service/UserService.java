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
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User registerUser(User user) {
        boolean exists = userRepository.findById(user.getId()).isPresent();
        if (exists) {
            throw new RuntimeException("이미 존재하는 ID입니다: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User login(String id, String password) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new Exception("해당 ID가 존재하지 않습니다."));
        if (!user.getPassword().equals(password)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getId());
    }

    @Transactional(readOnly = true)
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
    }

}
