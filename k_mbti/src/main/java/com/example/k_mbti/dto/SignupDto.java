package com.example.k_mbti.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {


    private String email;
    private String nickname;
    private String password;
    private String passwordConfirm;
}
