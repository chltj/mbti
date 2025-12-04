package com.example.k_mbti.controller;

import com.example.k_mbti.dto.LoginDto;
import com.example.k_mbti.dto.SignupDto;
import com.example.k_mbti.dto.UserDto;
import com.example.k_mbti.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    // ✅ 생성자에 AuthService 받음 → 이걸 스프링이 주입해야 하는데 지금은 못 찾고 있었던 것
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

   @PostMapping("/login")
public String login(LoginDto loginDto,
                    HttpSession session,
                    RedirectAttributes rttr) {

    UserDto user = authService.login(loginDto);

    if (user == null) {
        rttr.addFlashAttribute("loginError", "이메일 또는 비밀번호가 일치하지 않습니다.");
        return "redirect:/login";
    }

    // 로그인 성공 → 세션 저장
    session.setAttribute("loginUser", user);
    return "redirect:/";
}


    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "signup";
    }

@PostMapping("/signup")
public String signup(@ModelAttribute SignupDto signupDto, Model model) {

    try {
        authService.signup(signupDto);  // DB에 사용자 저장
    } catch (IllegalArgumentException e) {
        // 회원가입 실패 → 메시지 띄우고 다시 signup 페이지로
        model.addAttribute("signupError", e.getMessage());
        return "signup";
    }

    // ⭐ 회원가입 성공 → 로그인 페이지로 강제 이동
    return "redirect:/login";
}
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
}
@GetMapping("/kakao-login")
public String kakaoLogin(@RequestParam("nickname") String nickname,
                         @RequestParam(value = "email", required = false) String email,
                         HttpSession session) {

    // 서비스에서 카카오 유저 처리 (없으면 회원가입처럼 생성, 있으면 그대로 사용)
    UserDto user = authService.kakaoLogin(email, nickname);

    // ★ 세션에 로그인 사용자 저장 → 헤더에서 session.loginUser 로 사용
    session.setAttribute("loginUser", user);

    // 메인 페이지로 이동
    return "redirect:/";
}

}
