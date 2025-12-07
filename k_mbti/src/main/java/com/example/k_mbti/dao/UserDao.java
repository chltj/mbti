package com.example.k_mbti.dao;

import com.example.k_mbti.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    UserDto findByEmail(@Param("email") String email);
    void insertUser(UserDto user);
    void updateProfile(UserDto user);
    UserDto findById(@Param("id") Long id);

}

