package com.example.k_mbti.dao;

import com.example.k_mbti.dto.ChatRoomDto;
import com.example.k_mbti.dto.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDao {

    // 방
    int insertRoom(ChatRoomDto room);

    // 🔥 roomId로 통일
    ChatRoomDto findRoomById(@Param("roomId") Long roomId);

    List<ChatRoomDto> findAllRooms();

    List<ChatRoomDto> findRoomsByMember(@Param("nickname") String nickname);

    // 방 멤버
    void insertMember(@Param("roomId") Long roomId,
                      @Param("nickname") String nickname);

    List<String> findMembersByRoomId(@Param("roomId") Long roomId);

    // 메시지
    void insertMessage(ChatMessageDto message);

    List<ChatMessageDto> findMessagesByRoomId(@Param("roomId") Long roomId);

    void updateMemberNickname(@Param("oldNickname") String oldNickname,
                              @Param("newNickname") String newNickname);
}
