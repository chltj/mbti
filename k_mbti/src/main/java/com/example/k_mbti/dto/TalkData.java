package com.example.k_mbti.dto;

import java.time.LocalDateTime;

public class TalkData {
    private LocalDateTime timestamp; // 대화 시간 (응답 속도 분석용)
    private String speaker;          // 발화자 이름
    private String content;          // 대화 내용

    // 기본 생성자
    public TalkData() {
    }

    // --- 분석에 사용될 유틸리티 메소드 ---

    public boolean isEmoticon() {
        // 이모티콘, 사진, 동영상 메시지를 포함 (Warmth Score 계산용)
        return content.contains("이모티콘") || content.contains("사진") || content.contains("동영상");
    }

    public boolean isQuestion() {
        // 간단하게 "?"로 끝나는 경우를 질문으로 판단 (Initiative Score 계산용)
        return content.trim().endsWith("?");
    }

    public boolean isNonTalk() {
        // [파일 첨부], [OOO님이 나가셨습니다] 등 실제 대화가 아닌 시스템 메시지 필터링
        return content.startsWith("[") && content.endsWith("]");
    }

    // --- Getter 및 Setter ---

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}