package com.example.k_mbti.dao;

import com.example.k_mbti.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    // 이메일로 찾기 (회원가입 중복체크용으로 계속 써도 됨)
    UserDto findByEmail(@Param("email") String email);

    // ✅ 로그인용: login_id로 유저 찾기
    UserDto findByLoginId(@Param("loginId") String loginId);

    void insertUser(UserDto user);

    void updateProfile(UserDto user);

    UserDto findById(@Param("id") Long id);
    
}
