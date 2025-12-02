package com.example.k_mbti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {

        String kakaoLoginUrl = "/oauth2/authorization/kakao";
        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);

        return "login";   // login.jsp
    }

    // 로그인 후 메인 페이지
    @GetMapping("/main")
    public String mainPage() {
        return "main";    // main.jsp
    }
}
