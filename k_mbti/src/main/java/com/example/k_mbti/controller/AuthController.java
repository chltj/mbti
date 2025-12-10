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

    // 생성자 주입
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /* ==========================
       로그인
    ========================== */

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto,
                        HttpSession session,
                        RedirectAttributes rttr) {

        // AuthServiceImpl.login() 은 loginId로 로그인 처리 중
        UserDto user = authService.login(loginDto);

        if (user == null) {
            // 🔹 문구를 아이디 기준으로 변경
            rttr.addFlashAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/login";
        }

        // 로그인 성공 → 세션에 유저 저장
        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    /* ==========================
       회원가입
    ========================== */

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupDto signupDto,
                         Model model) {

        try {
            // AuthServiceImpl.signup() 안에서
            //  - 이메일 중복 체크
            //  - 아이디(loginId) 중복 체크
            //  - 비밀번호 암호화 후 저장
            authService.signup(signupDto);
        } catch (IllegalArgumentException e) {
            // 회원가입 실패 → 메시지 + 이전 입력값 다시 전달
            model.addAttribute("signupDto", signupDto);
            model.addAttribute("signupError", e.getMessage()); 
            // 예: "이미 사용 중인 이메일입니다.", "이미 사용 중인 아이디입니다."
            return "signup";
        }

        // 회원가입 성공 → 로그인 페이지로 이동
        return "redirect:/login";
    }

    /* ==========================
       로그아웃
    ========================== */

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /* ==========================
       카카오 로그인
    ========================== */

    @GetMapping("/kakao-login")
    public String kakaoLogin(@RequestParam("nickname") String nickname,
                             @RequestParam(value = "email", required = false) String email,
                             HttpSession session) {

        // 서비스에서 카카오 유저 처리 (없으면 회원가입처럼 생성, 있으면 기존 유저 반환)
        UserDto user = authService.kakaoLogin(email, nickname);

        // 세션에 로그인 사용자 저장 → 헤더에서 ${session.loginUser} 로 사용
        session.setAttribute("loginUser", user);

        // 메인 페이지로 이동
        return "redirect:/";
    }
}
