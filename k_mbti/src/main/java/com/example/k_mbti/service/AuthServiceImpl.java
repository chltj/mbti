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

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto findById(Long id) {
        return userDao.findById(id);
    }

@Override
public void updateProfile(UserDto user) {

    // 🔹 비밀번호가 들어온 경우에만 암호화해서 업데이트
    if (user.getPassword() != null && !user.getPassword().isBlank()) {
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    } else {
        // 비밀번호 변경 안 할 때는 null로 두면 Mapper에서 건드리지 않게 함
        user.setPassword(null);
    }

    // 🔍 디버그용 로그 (한 번 확인해보고 나중에 지워도 됨)
    System.out.println(
            "[AuthServiceImpl.updateProfile] id=" + user.getId()
                    + ", nickname=" + user.getNickname()
                    + ", email=" + user.getEmail()
                    + ", phone=" + user.getPhone()
                    + ", encodedPassword=" + user.getPassword()
    );

    userDao.updateProfile(user);
}



    /**
     * 카카오 로그인
     */
    @Override
    public UserDto kakaoLogin(String email, String nickname) {

        UserDto user = null;

        // 1) 이메일이 있으면 이메일로 먼저 조회
        if (email != null && !email.isEmpty()) {
            user = userDao.findByEmail(email);
        }

        // 2) 기존 유저가 없으면 새로 만들어 저장 (간단 회원가입 느낌)
        if (user == null) {
            user = new UserDto();

            // 이메일 권한을 안 준 경우를 대비해 임시 이메일 생성
            if (email == null || email.isEmpty()) {
                email = nickname + "@kakao.local";
            }

            // 🔹 loginId도 필수이므로 생성해 줘야 함
            //    여기서는 이메일 앞부분 + 접두어로 간단히 만듦
            String baseLoginId = email.split("@")[0];       // 예: test@kakao.com → "test"
            String loginId = "kakao_" + baseLoginId;

            // 혹시라도 중복될 수 있으니 간단히 한번 체크 (필요시 더 강화 가능)
            if (userDao.findByLoginId(loginId) != null) {
                loginId = loginId + "_" + System.currentTimeMillis();
            }

            user.setLoginId(loginId);
            user.setEmail(email);
            user.setNickname(nickname);
            user.setPassword("");  // 카카오 계정이므로 비번 직접 로그인 안 씀
            user.setPhone(null);   // 카카오에서 전화번호 가져오지 않는 경우가 많음
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
        UserDto existingByEmail = userDao.findByEmail(signupDto.getEmail());
        if (existingByEmail != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 로그인 아이디 중복 체크
        UserDto existingByLoginId = userDao.findByLoginId(signupDto.getLoginId());
        if (existingByLoginId != null) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 3. UserDto로 변환해서 저장
        UserDto user = new UserDto();
        user.setLoginId(signupDto.getLoginId());                      // 🔹 아이디
        user.setEmail(signupDto.getEmail());                          // 이메일
        user.setNickname(signupDto.getNickname());                    // 이름
        user.setPassword(passwordEncoder.encode(signupDto.getPassword())); // 비밀번호 해시
        user.setPhone(signupDto.getPhone());                          // 전화번호
        user.setCreatedAt(LocalDateTime.now());

        userDao.insertUser(user);
    }

    /**
     * 일반 로그인 (login_id로 로그인)
     */
    @Override
    public UserDto login(LoginDto loginDto) {

        // 1. login_id로 유저 찾기
        UserDto user = userDao.findByLoginId(loginDto.getLoginId());
        if (user == null) {
            // 아이디 없음 → 로그인 실패
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
