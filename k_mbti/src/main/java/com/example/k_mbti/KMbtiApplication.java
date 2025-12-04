package com.example.k_mbti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.k_mbti.dao")
public class KMbtiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KMbtiApplication.class, args);
	}

}
