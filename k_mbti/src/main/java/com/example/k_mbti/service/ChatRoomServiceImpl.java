package com.example.k_mbti.service;

import com.example.k_mbti.dao.ChatDao;
import com.example.k_mbti.dto.ChatRoomDto;
import com.example.k_mbti.dto.ChatMessageDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatDao chatDao;

    public ChatRoomServiceImpl(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @Override
    public List<ChatRoomDto> getRoomList() {
        return chatDao.findAllRooms();
    }

    @Override
    public ChatRoomDto createRoom(String name, int maxCount, String creatorNickname) {
        ChatRoomDto room = new ChatRoomDto();
        room.setName(name);
        room.setMaxCount(maxCount);
        room.setOwner(creatorNickname);

        // 1) 방 저장
        chatDao.insertRoom(room);          // room.id 가 자동으로 채워짐

        // 2) 방장도 멤버로 추가
        chatDao.insertMember(room.getId(), creatorNickname);

        return chatDao.findRoomById(room.getId());
    }

    @Override
    public ChatRoomDto getRoom(Long roomId) {
        return chatDao.findRoomById(roomId);
    }

    @Override
    public boolean joinRoom(Long roomId, String nickname) {
        ChatRoomDto room = chatDao.findRoomById(roomId);
        if (room == null) return false;

        // 인원 가득 찼는지 체크
        if (room.getCurrentCount() >= room.getMaxCount()) {
            return false;
        }

        // 이미 멤버인지 체크 (중복 방지)
        List<String> members = chatDao.findMembersByRoomId(roomId);
        if (members.contains(nickname)) {
            return true; // 이미 들어가 있으니 OK 처리
        }

        chatDao.insertMember(roomId, nickname);
        return true;
    }

    @Override
    public void sendMessage(Long roomId, String sender, String content) {
        if (content == null || content.trim().isEmpty()) return;

        ChatMessageDto msg = new ChatMessageDto();
        msg.setRoomId(roomId);
        msg.setSender(sender);
        msg.setContent(content.trim());
        msg.setSentAt(LocalDateTime.now());

        chatDao.insertMessage(msg);
    }

    @Override
    public List<ChatMessageDto> getMessages(Long roomId) {
        return chatDao.findMessagesByRoomId(roomId);
    }

    @Override
    public List<ChatRoomDto> getRoomsByMember(String nickname) {
        return chatDao.findRoomsByMember(nickname);
    }

    @Override
    public List<String> getMembers(Long roomId) {
        return chatDao.findMembersByRoomId(roomId);
    }
}
