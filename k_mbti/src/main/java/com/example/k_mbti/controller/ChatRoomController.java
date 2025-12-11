package com.example.k_mbti.controller;

import com.example.k_mbti.dto.ChatRoomDto;
import com.example.k_mbti.dto.ChatMessageDto;
import com.example.k_mbti.dto.UserDto;
import com.example.k_mbti.service.ChatRoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    /** 소통의 방 목록 + 방 만들기 */
@GetMapping("/rooms")
public String roomList(HttpSession session,
                       Model model) {

    UserDto loginUser = (UserDto) session.getAttribute("loginUser");
    if (loginUser == null) {
        model.addAttribute("errorMsg", "로그인 후 이용 가능합니다.");
        return "login"; // 실제 로그인 페이지로 변경
    }

    // 전체 방 목록
    List<ChatRoomDto> rooms = chatRoomService.getRoomList();

    // ✅ 내가 참여한 방 목록
    List<ChatRoomDto> myRooms = chatRoomService.getRoomsByMember(loginUser.getNickname());

    // ✅ 내가 참여한 방의 id 목록만 추출
    List<Long> joinedRoomIds = myRooms.stream()
            .map(ChatRoomDto::getId)
            .toList(); // 자바 17 이상. 8~11이면 collect(Collectors.toList())

    model.addAttribute("rooms", rooms);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("joinedRoomIds", joinedRoomIds); // 🔥 추가

    return "chat/rooms";
}


    /** 방 생성 */
    @PostMapping("/rooms")
    public String createRoom(@RequestParam String name,
                             @RequestParam int maxCount,
                             HttpSession session,
                             RedirectAttributes rttr) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            rttr.addFlashAttribute("errorMsg", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        if (maxCount < 2) maxCount = 2;

        ChatRoomDto room = chatRoomService.createRoom(name, maxCount, loginUser.getNickname());
        return "redirect:/chat/rooms/" + room.getId();
    }

    /** 방 입장 (인원 체크 후 입장) */
    @PostMapping("/rooms/{id}/join")
    public String joinRoom(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes rttr) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            rttr.addFlashAttribute("errorMsg", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        boolean joined = chatRoomService.joinRoom(id, loginUser.getNickname());
        if (!joined) {
            rttr.addFlashAttribute("errorMsg", "이미 인원이 가득 찬 방입니다.");
            return "redirect:/chat/rooms";
        }

        return "redirect:/chat/rooms/" + id;
    }

    /** 방 내부 화면 */
    @GetMapping("/rooms/{id}")
    public String roomDetail(@PathVariable Long id,
                             HttpSession session,
                             Model model,
                             RedirectAttributes rttr) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            rttr.addFlashAttribute("errorMsg", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        ChatRoomDto room = chatRoomService.getRoom(id);
        if (room == null) {
            rttr.addFlashAttribute("errorMsg", "해당 방을 찾을 수 없습니다.");
            return "redirect:/chat/rooms";
        }

        List<ChatMessageDto> messages = chatRoomService.getMessages(id);

        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        model.addAttribute("loginUser", loginUser);

        return "chat/room"; // templates/chat/room.html
    }

    /** 메시지 전송 */
    @PostMapping("/rooms/{id}/message")
    public String sendMessage(@PathVariable Long id,
                              @RequestParam String content,
                              HttpSession session,
                              RedirectAttributes rttr) {

        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            rttr.addFlashAttribute("errorMsg", "로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        chatRoomService.sendMessage(id, loginUser.getNickname(), content);
        return "redirect:/chat/rooms/" + id;
    }
}
