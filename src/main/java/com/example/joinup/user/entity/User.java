package com.example.joinup.user.entity;

import com.example.joinup.competitionboard.entity.CompetitionPage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id", nullable = false, unique = true, length = 10)
    @Size(min = 3, max = 10, message = "ID는 3자 이상, 10자 이하로 입력해야 합니다.")
    private String id;

    @Column(name = "password", nullable = false, length = 10)
    private String password;

    @Column(name = "real_name", nullable = false, length = 20)
    private String realName;

    @Column(name = "birth", nullable = false, length = 8)
    private String birth;

    @Column(name = "phone", nullable = false, unique = true, length = 11)
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하고 뒤에 숫자 8자가 와야 합니다.")
    private String phone;

    // CompetitionPage와의 연관관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CompetitionPage> competitionPages = new ArrayList<>();

    public String getUsername() {
        return this.id; // ID 필드를 사용자 이름으로 간주
    }
    public String getName() {
        return realName;
    }


}
