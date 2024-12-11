package com.example.joinup;

import com.example.joinup.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JoinupApplicationTests { // 클래스 이름 수정

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testJwtUtilBean() {
		Object jwtUtilBean = applicationContext.getBean(JwtUtil.class);
		assertNotNull(jwtUtilBean, "JwtUtil 빈이 생성되지 않았습니다!");
	}
}
