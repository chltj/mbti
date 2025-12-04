package com.example.k_mbti.service;

import com.example.k_mbti.dto.LoginDto;
import com.example.k_mbti.dto.SignupDto;
import com.example.k_mbti.dto.UserDto;

public interface AuthService {

    void signup(SignupDto signupDto);

    UserDto login(LoginDto loginDto);

    UserDto kakaoLogin(String email, String nickname);
}
