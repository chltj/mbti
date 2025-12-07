package com.example.k_mbti.dto;

import java.time.LocalDateTime;

public class ChatMessageDto {

    private Long roomId;          // 방 ID
    private String sender;        // 닉네임
    private String content;       // 내용
    private LocalDateTime sentAt; // 보낸 시간

    public ChatMessageDto() {}

    public ChatMessageDto(Long roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
