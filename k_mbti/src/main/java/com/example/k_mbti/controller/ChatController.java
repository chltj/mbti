package com.example.k_mbti.controller;

import com.example.k_mbti.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {

    private final ChatService chatService;

    // ⭐ 생성자에서 ChatService 주입
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String showChatPage(@RequestParam(required = false) String mbti, Model model) {
        model.addAttribute("selectedMbti", mbti != null ? mbti : "ENFP");
        return "chat"; // templates/chat.html
    }

    @PostMapping("/chat/message")
    @ResponseBody
    public Map<String, String> sendMessage(@RequestParam String mbti,
                                           @RequestParam String message) {

        // ⭐ 여기에서 ChatService 사용
        String reply = chatService.generateReply(mbti, message);

        Map<String, String> res = new HashMap<>();
        res.put("reply", reply);
        return res;  // {"reply": "..."} JSON
    }
}
