package com.example.joinup.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// User 엔티티는 애플리케이션 사용자 정보를 관리하는 클래스입니다.
// 데이터베이스의 'users' 테이블과 매핑됩니다.
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    // 'userId'는 사용자의 고유 ID로, 기본 키 역할을 합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동으로 ID 값 생성
    @Column(name = "user_id")
    private Long userId;

    // 'id'는 사용자의 로그인 ID를 나타냅니다.
    // ID는 필수값이며, 길이는 3자 이상, 10자 이하로 설정됩니다.
    // unique 제약 조건으로 중복된 값 저장 불가
    @Column(name = "id", nullable = false, unique = true, length = 10) // 로그인 ID
    @Size(min = 3, max = 10, message = "ID는 3자 이상, 10자 이하로 입력해야 합니다.")
    private String id; // 로그인 ID로 사용

    // 'password'는 사용자의 비밀번호를 나타냅니다.
    // 필수값이며 최대 길이는 10자입니다.
    @Column(name = "password", nullable = false, length = 10) // 비밀번호
    private String password;

    // 'realName'은 사용자의 실제 이름을 나타냅니다.
    // 필수값이며 최대 길이는 20자입니다.
    @Column(name = "real_name", nullable = false, length = 20) // 이름
    private String realName;

    // 'birth'는 사용자의 생년월일을 나타냅니다.
    // 필수값이며 길이는 8자(YYYYMMDD)로 제한됩니다.
    @Column(name = "birth", nullable = false, length = 8) // 생년월일
    private String birth;

    // 'phone'은 사용자의 전화번호를 나타냅니다.
    // 필수값이며, 010으로 시작하고 숫자 8자리로 이루어져야 합니다.
    // unique 제약 조건으로 중복된 값 저장 불가
    @Column(name = "phone", nullable = false, unique = true, length = 11) // 전화번호
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하고 뒤에 숫자 8자가 와야 합니다.")
    private String phone;

    // Getter 및 Setter 메서드


}