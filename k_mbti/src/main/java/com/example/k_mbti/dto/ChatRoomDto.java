package com.example.k_mbti.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomDto {

    private Long id;              // 방 ID
    private String name;          // 방 이름
    private int maxCount;         // 최대 인원
    private int currentCount;     // 현재 인원
    private String owner;                // ✅ 방장 닉네임 추가
    private List<String> members = new ArrayList<>();

    public ChatRoomDto() {}

    public ChatRoomDto(Long id, String name, int maxCount, String creatorNickname) {
        this.id = id;
        this.name = name;
        this.maxCount = maxCount;
        this.currentCount = 1;
        this.owner = creatorNickname;    // ✅ 방장 저장
        this.members = new ArrayList<>();
        this.members.add(creatorNickname); 
    }

    public boolean isFull() {
        return currentCount >= maxCount;
    }

    public boolean join(String nickname) {
        if (isFull()) return false;
        if (!members.contains(nickname)) {
            members.add(nickname);
            currentCount++;
        }
        return true;
    }

    // --- getter / setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMaxCount() { return maxCount; }
    public void setMaxCount(int maxCount) { this.maxCount = maxCount; }

    public int getCurrentCount() { return currentCount; }
    public void setCurrentCount(int currentCount) { this.currentCount = currentCount; }

    public String getOwner() { return owner; }          // ✅ 추가
    public void setOwner(String owner) { this.owner = owner; }

    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
}
