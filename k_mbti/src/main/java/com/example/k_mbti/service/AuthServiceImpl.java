package com.example.k_mbti.service;

import com.example.k_mbti.dao.UserDao;
import com.example.k_mbti.dto.LoginDto;
import com.example.k_mbti.dto.SignupDto;
import com.example.k_mbti.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 생성자 주입
    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 카카오 로그인 (이미 잘 구현되어 있음)
     */
    @Override
    public UserDto kakaoLogin(String email, String nickname) {

        UserDto user = null;

        // 이메일이 있으면 이메일로 먼저 조회
        if (email != null && !email.isEmpty()) {
            user = userDao.findByEmail(email);
        }

        // 기존 유저가 없으면 새로 만들어 저장 (간단 회원가입 느낌)
        if (user == null) {
            user = new UserDto();
            // 이메일 권한을 안 준 경우를 대비해 임시 이메일 생성
            if (email == null || email.isEmpty()) {
                email = nickname + "@kakao.local";
            }
            user.setEmail(email);
            user.setNickname(nickname);
            user.setPassword("");  // 카카오 계정이므로 비밀번호는 사용 안 함
            user.setCreatedAt(LocalDateTime.now());

            userDao.insertUser(user);
        }

        return user;
    }

    /**
     * 일반 회원가입
     */
    @Override
    public void signup(SignupDto signupDto) {

        // 1. 이메일 중복 체크
        UserDto existing = userDao.findByEmail(signupDto.getEmail());
        if (existing != null) {
            // 컨트롤러에서 처리할 수 있도록 런타임 예외 던지기
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 2. UserDto로 변환해서 저장
        UserDto user = new UserDto();
        user.setEmail(signupDto.getEmail());
        user.setNickname(signupDto.getNickname());
        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userDao.insertUser(user);
    }

    /**
     * 일반 로그인
     */
    @Override
    public UserDto login(LoginDto loginDto) {

        // 1. 이메일로 유저 찾기
        UserDto user = userDao.findByEmail(loginDto.getEmail());
        if (user == null) {
            // 이메일 없음 → 로그인 실패
            return null;
        }

        // 2. 비밀번호 일치 확인
        boolean matches = passwordEncoder.matches(
                loginDto.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            // 비밀번호 불일치 → 로그인 실패
            return null;
        }

        // 3. 로그인 성공 → 유저 정보 반환
        return user;
    }
}
