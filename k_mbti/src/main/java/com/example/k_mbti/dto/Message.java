package com.example.k_mbti.dto;

import java.time.LocalDateTime;

public class Message {

    private String sender;
    private LocalDateTime time;
    private String content;

    public Message(String sender, LocalDateTime time, String content) {
        this.sender = sender;
        this.time = time;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
