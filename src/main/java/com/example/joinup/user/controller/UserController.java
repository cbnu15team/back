package com.example.joinup.user.controller;

import com.example.joinup.user.UserDTO.UserDTO;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.repository.UserRepository;
import com.example.joinup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 주소
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
// 반환 타입을 Map<String, String>으로 수정
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO loginRequest) {
        System.out.println("로그인 요청: ID=" + loginRequest.getId() + ", Password=" + loginRequest.getPassword());
        try {
            // 사용자 로그인 (서비스 레이어로 처리 위임)
            User user = userService.login(loginRequest.getId(), loginRequest.getPassword());

            // JWT 토큰 생성
            String token = userService.generateToken(user);

            // 성공 시 JWT 토큰 반환
            // ResponseEntity<String> → ResponseEntity<Map<String, String>>로 수정
            return ResponseEntity.ok(Map.of("token", token)); // Map 형식으로 반환

        } catch (Exception e) {
            // 에러 메시지도 Map 형식으로 반환하도록 수정
            return ResponseEntity.status(401).body(Map.of("error", "로그인 실패: " + e.getMessage()));
        }
    }
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage() {
        try {
            // 현재 인증된 사용자 ID 가져오기
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            // 사용자 정보 및 작성한 글 조회
            User user = userService.findById(userId);
            Map<String, Object> myPageData = userService.getMyPageData(user);

            return ResponseEntity.ok(myPageData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("마이페이지를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}