package com.example.joinup.user.service;

import com.example.joinup.user.entity.User;
import com.example.joinup.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public User login(String id, String password) {
        return userRepository.findByIdAndPassword(id, password)
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다."));
    }

    public Optional<User> findByUserId(Integer userId) {
        return userRepository.findById(userId); // userId 기반 조회
    }
}