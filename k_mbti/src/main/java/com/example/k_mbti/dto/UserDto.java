package com.example.k_mbti.dto;


import java.time.LocalDateTime;


public class UserDto {

    private Long id;              // PK
    private String loginId;       // 로그인 아이디 (login_id)
    private String email;         // 이메일
    private String nickname;      // 닉네임
    private String password;      // 해시된 비밀번호
    private String phone;         // 전화번호
    private LocalDateTime createdAt; // 가입일시

    // =============================
    // Getter / Setter 전부 직접 작성
    // =============================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
