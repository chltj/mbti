package com.example.k_mbti.service;

import com.example.k_mbti.dto.ChatRoomDto;
import com.example.k_mbti.dto.ChatMessageDto;

import java.util.List;

public interface ChatRoomService {

    // 방 목록
    List<ChatRoomDto> getRoomList();

    // 방 생성
    ChatRoomDto createRoom(String name, int maxCount, String creatorNickname);

    // 방 조회
    ChatRoomDto getRoom(Long roomId);

    // 방 입장 (인원 체크 + 멤버 추가)
    boolean joinRoom(Long roomId, String nickname);

    // 메시지 전송
    void sendMessage(Long roomId, String sender, String content);

    // 메시지 목록
    List<ChatMessageDto> getMessages(Long roomId);

    List<ChatRoomDto> getRoomsByMember(String myName);

    List<String> getMembers(Long roomId);
     void updateMemberNickname(String oldNickname, String newNickname);
}
