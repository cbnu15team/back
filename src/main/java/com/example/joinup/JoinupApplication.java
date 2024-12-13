package com.example.joinup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "com.example.joinup")
public class JoinupApplication {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(JoinupApplication.class, args);
	}

	@PostConstruct
	public void checkBeans() {
		if (applicationContext.containsBean("jwtAuthenticationFilter")) {
			System.out.println("JwtAuthenticationFilter 빈이 등록되어 있습니다.");
		} else {
			System.out.println("JwtAuthenticationFilter 빈을 찾을 수 없습니다.");
		}
	}
}
