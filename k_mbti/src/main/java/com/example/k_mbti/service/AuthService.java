package com.example.k_mbti.service;

import com.example.k_mbti.dto.LoginDto;
import com.example.k_mbti.dto.SignupDto;
import com.example.k_mbti.dto.UserDto;

public interface AuthService {

    void signup(SignupDto signupDto);      // 회원가입 (login_id + email + ...)

    UserDto login(LoginDto loginDto);      // 🔹 login_id로 로그인

    UserDto kakaoLogin(String email, String nickname);

    UserDto findById(Long id);

    void updateProfile(UserDto user);
}
