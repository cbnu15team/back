package com.example.joinup.user.controller;

import com.example.joinup.user.entity.User;
import com.example.joinup.user.repository.UserRepository;
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

            // 사용자 등록 (서비스 레이어로 처리 위임)
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("회원가입 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }



    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        System.out.println("로그인 요청: ID=" + loginRequest.getId() + ", Password=" + loginRequest.getPassword());
        try {
            // 사용자 로그인 (서비스 레이어로 처리 위임)
            User user = userService.login(loginRequest.getId(), loginRequest.getPassword());
            return ResponseEntity.ok(user); // 성공 시 사용자 정보 반환

        } catch (Exception e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
}