package com.example.k_mbti.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String nickname;
    private String password;      // 해시된 비밀번호
    private LocalDateTime createdAt;
}
