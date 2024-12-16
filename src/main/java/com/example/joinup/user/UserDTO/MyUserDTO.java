package com.example.joinup.user.UserDTO;

import com.example.joinup.user.entity.User;

public class MyUserDTO {
    private String id;          // 사용자 ID
    private String realName;    // 실제 이름
    private String birth;       // 생년월일
    private String phone;       // 전화번호

    public MyUserDTO(User user) {
        this.id = user.getId();
        this.realName = user.getRealName();
        this.birth = user.getBirth();
        this.phone = user.getPhone();
    }

    // Getter 메서드
    public String getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String getBirth() {
        return birth;
    }

    public String getPhone() {
        return phone;
    }
}
