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

    public ChatRoomServiceImpl(ChatDao chatRoomDao) {
        this.chatDao = chatRoomDao;
    }

    @Override
    public void updateMemberNickname(String oldNickname, String newNickname) {
        chatDao.updateMemberNickname(oldNickname, newNickname);
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

    // ✅ 1) 이미 멤버인지 먼저 체크 (재입장은 항상 허용)
    List<String> members = chatDao.findMembersByRoomId(roomId);
    System.out.println("joinRoom members = " + members + " / nickname = " + nickname);

    if (members.contains(nickname)) {
        // 내가 이미 이 방 멤버라면, 인원 제한과 상관없이 입장 허용
        System.out.println("이미 멤버이므로 인원 꽉 차도 입장 허용");
        return true;
    }

    // ✅ 2) 새로 들어가는 경우에만 인원 제한 검사
    System.out.println("현재 인원 = " + room.getCurrentCount() + ", 최대 인원 = " + room.getMaxCount());
    if (room.getCurrentCount() >= room.getMaxCount()) {
        System.out.println("멤버가 아니고, 방이 이미 가득 찼으므로 입장 불가");
        return false;
    }

    // ✅ 3) 멤버가 아니고, 인원도 여유 → 새 멤버 추가
    chatDao.insertMember(roomId, nickname);
    System.out.println("새 멤버 추가 완료: " + nickname);
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
