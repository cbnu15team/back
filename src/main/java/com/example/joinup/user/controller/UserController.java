package com.example.joinup.user.controller;

import com.example.joinup.user.entity.User;
import com.example.joinup.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // 요청 데이터 검증
            if (user.getBirth() == null || user.getBirth().isEmpty()) {
                return ResponseEntity.badRequest().body("생년월일은 필수 입력 항목입니다.");
            }

            // 중복된 아이디 검사
            if (userRepository.existsById(user.getId())) {
                return ResponseEntity.status(400).body("이미 존재하는 아이디입니다.");
            }

            // 사용자 저장
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원가입 실패: " + e.getMessage());
        }
    }



    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        String password = request.get("password");

        try {
            // 사용자 검증 로직만 수행
            User user = userService.login(id, password);
            System.out.println("로그인 성공: " + user.getId());
            return ResponseEntity.ok("로그인 성공!");
        } catch (Exception e) {
            System.err.println("로그인 실패: " + e.getMessage());
        }
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

